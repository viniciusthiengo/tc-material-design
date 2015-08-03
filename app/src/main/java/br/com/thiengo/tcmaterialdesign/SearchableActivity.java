package br.com.thiengo.tcmaterialdesign;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.thiengo.tcmaterialdesign.adapters.CarAdapter;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;
import br.com.thiengo.tcmaterialdesign.extras.UtilTCM;
import br.com.thiengo.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;
import br.com.thiengo.tcmaterialdesign.network.NetworkConnection;
import br.com.thiengo.tcmaterialdesign.network.Transaction;
import br.com.thiengo.tcmaterialdesign.provider.SearchableProvider;


public class SearchableActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, Transaction {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List<Car> mList;
    private CarAdapter adapter;
    private CoordinatorLayout clContainer;
    private ProgressBar mPbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList("mListAux");
        }
        else{
            mList = new ArrayList<>();
        }

        clContainer = (CoordinatorLayout) findViewById(R.id.cl_container);

        mPbLoad = (ProgressBar) findViewById(R.id.pb_load);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager( this );
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        adapter = new CarAdapter(this, mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        hendleSearch( getIntent() );
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch( Intent intent ){
        if( Intent.ACTION_SEARCH.equalsIgnoreCase( intent.getAction() ) ){
            String q = intent.getStringExtra( SearchManager.QUERY );

            mToolbar.setTitle(q);
            filterCars( q );

            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery( q, null );
        }
    }


    public void filterCars( String q ){
        mList.clear();
        NetworkConnection.getInstance(this).execute(this, SearchableActivity.class.getName());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("mListAux", (ArrayList<Car>) mList);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(this).getRequestQueue().cancelAll(SearchableActivity.class.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchable_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        else if( id == R.id.action_delete ){
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);

            searchRecentSuggestions.clearHistory();

            Toast.makeText(this, "Cookies removidos", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    // LISTENERS
    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(this, CarActivity.class);
        intent.putExtra("car", mList.get(position));

        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

            View ivCar = view.findViewById(R.id.iv_car);
            View tvModel = view.findViewById(R.id.tv_model);
            View tvBrand = view.findViewById(R.id.tv_brand);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create(ivCar, "element1"),
                    Pair.create( tvModel, "element2" ),
                    Pair.create( tvBrand, "element3" ));

            startActivity(intent, options.toBundle() );
        }
        else{
            startActivity(intent);
        }
    }

    @Override
    public void onLongPressClickListener(View view, int position) {}


    // NETWORK
        @Override
        public WrapObjToNetwork doBefore() {
            mPbLoad.setVisibility( View.VISIBLE );

            if( UtilTCM.verifyConnection(this) ){
                Car car = new Car();
                car.setCategory(0);

                if( mList != null && mList.size() > 0 ){
                    car.setId( mList.get(mList.size() - 1).getId() );
                }

                return( new WrapObjToNetwork(car, "get-cars-search", mToolbar.getTitle().toString() ) );
            }
            return null;
        }

        @Override
        public void doAfter(JSONArray jsonArray) {
            mPbLoad.setVisibility(View.GONE );

            if( jsonArray != null ){
                CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
                Gson gson = new Gson();

                try{
                    for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                        Car car = gson.fromJson( jsonArray.getJSONObject( i ).toString(), Car.class );
                        adapter.addListItem(car, mList.size());
                    }
                }
                catch(JSONException e){
                    Log.i("LOG", "doAfter(): "+e.getMessage());
                }
            }
            else{
                Toast.makeText(this, "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
            }


            mRecyclerView.setVisibility(mList.isEmpty() ? View.GONE : View.VISIBLE);
            if( mList.isEmpty() ){
                TextView tv = new TextView( this );
                tv.setText( "Nenhum carro encontrado." );
                tv.setTextColor( getResources().getColor( R.color.colorPrimarytext ) );
                tv.setId( 1 );
                tv.setLayoutParams( new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT )  );
                tv.setGravity(Gravity.CENTER);

                clContainer.addView( tv );
            }
            else if( clContainer.findViewById(1) != null ) {
                clContainer.removeView( clContainer.findViewById(1) );
            }
        }
}
