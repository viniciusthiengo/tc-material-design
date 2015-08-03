package br.com.thiengo.tcmaterialdesign.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.thiengo.tcmaterialdesign.CarActivity;
import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.adapters.CarAdapter;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;
import br.com.thiengo.tcmaterialdesign.extras.UtilTCM;
import br.com.thiengo.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;
import br.com.thiengo.tcmaterialdesign.network.NetworkConnection;
import br.com.thiengo.tcmaterialdesign.network.Transaction;
import de.greenrobot.event.EventBus;


public class CarFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {

    protected static final String TAG = "LOG";
    protected RecyclerView mRecyclerView;
    protected List<Car> mList;
    protected android.support.design.widget.FloatingActionButton fab;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected Activity mActivity;
    protected ProgressBar mPbLoad;
    protected boolean isLastItem;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        EventBus.getDefault().register(this);
        mActivity = activity;
    }


    // EVENT BUS LISTENER
    public void onEvent(Car car){
        for(int i = 0; i < mList.size(); i++){
            if( mList.get(i).getUrlPhoto().equalsIgnoreCase( car.getUrlPhoto() )
                && this.getClass().getName().equalsIgnoreCase( CarFragment.class.getName() )){

                Intent it = new Intent(mActivity, CarActivity.class);
                it.putExtra("car", mList.get(i));
                mActivity.startActivity(it);
                break;
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_car, container, false);

        mList = new ArrayList<>();
        mPbLoad = (ProgressBar) view.findViewById(R.id.pb_load);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();

                if ( !isLastItem
                        && mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1
                        && (mSwipeRefreshLayout == null || !mSwipeRefreshLayout.isRefreshing()) ) {
                    NetworkConnection.getInstance(getActivity()).execute(CarFragment.this, CarFragment.class.getName());
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        CarAdapter adapter = new CarAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
        setFloatingActionButton(view);

        activateSwipRefresh(view, this, CarFragment.class.getName());

        return view;
    }


    public void activateSwipRefresh(final View view, final Transaction transaction, final String tag){
        // SWIPE REFRESH LAYOUT
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (UtilTCM.verifyConnection(getActivity())) {
                    NetworkConnection.getInstance(getActivity()).execute(transaction, tag);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);

                    android.support.design.widget.Snackbar.make(view, "Sem conexão com Internet. Por favor, verifique sey WiFi ou 3G.", android.support.design.widget.Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(it);
                                }
                            })
                            .setActionTextColor(getActivity().getResources().getColor(R.color.coloLink))
                            .show();
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList("mList");

            if( mList == null || mList.size() == 0 ){
                callVolleyRequest();
            }
        }
        else{
            callVolleyRequest();
        }
    }


    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, CarFragment.class.getName() );
    }

    public void setFloatingActionButton(final View view){
        fab = (android.support.design.widget.FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.design.widget.Snackbar.make(view, "FAB clicked", android.support.design.widget.Snackbar.LENGTH_SHORT)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getActivity().getResources().getColor(R.color.coloLink))
                        .show();
            }
        });
    }


    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(getActivity(), CarActivity.class);
        intent.putExtra("car", mList.get(position));

        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

            View ivCar = view.findViewById(R.id.iv_car);
            View tvModel = view.findViewById(R.id.tv_model);
            View tvBrand = view.findViewById(R.id.tv_brand);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create( ivCar, "element1" ),
                    Pair.create( tvModel, "element2" ),
                    Pair.create( tvBrand, "element3" ));

            getActivity().startActivity( intent, options.toBundle() );
        }
        else{
            getActivity().startActivity(intent);
        }
    }


    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): "+position, Toast.LENGTH_SHORT).show();
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildAdapterPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    boolean callContextMenuStatus = false;
                    if( cv instanceof CardView ){
                        float x = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getX();
                        float w = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getWidth();
                        float y;// = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getY();
                        float h = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getHeight();

                        Rect rect = new Rect();
                        ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getGlobalVisibleRect(rect);
                        y = rect.top;

                        if( e.getX() >= x && e.getX() <= w + x && e.getRawY() >= y && e.getRawY() <= h + y ){
                            callContextMenuStatus = true;
                        }
                    }


                    if(cv != null && mRecyclerViewOnClickListenerHack != null && !callContextMenuStatus){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildAdapterPosition(cv) );
                    }

                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {}
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mList", (ArrayList<Car>) mList);
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll( CarFragment.class.getName() );
    }

    // NETWORK
        @Override
        public WrapObjToNetwork doBefore() {
            mPbLoad.setVisibility( (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) ? View.GONE : View.VISIBLE );

            if( UtilTCM.verifyConnection(getActivity()) ){
                Car car = new Car();
                car.setCategory(0);

                if( mList != null && mList.size() > 0 ){
                    car.setId( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ? mList.get(0).getId() : mList.get(mList.size() - 1).getId() );
                }

                return( new WrapObjToNetwork(car, "get-cars", (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) ) );
            }
            return null;
        }

        @Override
        public void doAfter(JSONArray jsonArray) {
            mPbLoad.setVisibility(View.GONE );

            if( jsonArray != null ){
                CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
                Gson gson = new Gson();
                int auxPosition = 0, position;

                if( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ){
                    mSwipeRefreshLayout.setRefreshing(false);
                    auxPosition = 1;
                }

                try{
                    for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                        Car car = gson.fromJson( jsonArray.getJSONObject( i ).toString(), Car.class );
                        position = auxPosition == 0 ? mList.size() : 0;
                        adapter.addListItem(car, position);

                        if( auxPosition == 1 ){
                            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                        }
                    }

                    if( jsonArray.length() == 0 && auxPosition == 0 ){
                        isLastItem = true;
                    }

                }
                catch(JSONException e){
                    Log.i(TAG, "doAfter(): "+e.getMessage());
                }
            }
            else{
                Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }

}
