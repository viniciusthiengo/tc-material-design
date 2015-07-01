package br.com.thiengo.tcmaterialdesign.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/*import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListenerAdapter;*/

import java.util.ArrayList;
import java.util.List;

import br.com.thiengo.tcmaterialdesign.CarActivity;
import br.com.thiengo.tcmaterialdesign.MainActivity;
import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.adapters.CarAdapter;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.extras.UtilTCM;
import br.com.thiengo.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;
import de.greenrobot.event.EventBus;

public class CarFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener {
    protected static final String TAG = "LOG";
    protected RecyclerView mRecyclerView;
    protected List<Car> mList;
    //private FloatingActionButton fab;
    //private ActionButton fab;
    //protected FloatingActionMenu fab;
    protected android.support.design.widget.FloatingActionButton fab;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList("mList");
        }
        else{
            mList = ((MainActivity) getActivity()).getCarsByCategory(0);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        EventBus.getDefault().register(this);
        mActivity = activity;
    }


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

                if(dy > 0){
                    //fab.hideMenuButton(true);
                }
                else{
                    //fab.showMenuButton(true);
                }

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //GridLayoutManager llm = (GridLayoutManager) mRecyclerView.getLayoutManager();
                /*StaggeredGridLayoutManager llm = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                int[] aux = llm.findLastCompletelyVisibleItemPositions(null);
                int max = -1;
                for(int i = 0; i < aux.length; i++){
                    max = aux[i] > max ? aux[i] : max;
                }*/

                CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                //if (mList.size() == max + 1) {
                    //List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10);
                    List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10, 0);
                    ((MainActivity) getActivity()).getListCars().addAll( listAux );

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerView, this ));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //llm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(llm);


        /*GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);*/

        /*StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        llm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(llm);*/

        //mList = ((MainActivity) getActivity()).getSetCarList(10);
        CarAdapter adapter = new CarAdapter(getActivity(), mList);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
        setFloatingActionButton(view);

        // SWIPE REFRESH LAYOUT
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipe);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if( UtilTCM.verifyConnection( getActivity() ) ){
                        CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();

                        List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(2, 0);
                        ((MainActivity) getActivity()).getListCars().addAll(listAux);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), 0);
                            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
                        }

                        new Thread(new Runnable(){
                            public void run(){
                                SystemClock.sleep(2000);
                                getActivity().runOnUiThread(new Runnable(){
                                    public void run(){
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }).start();
                    }
                    else{
                        mSwipeRefreshLayout.setRefreshing(false);

                        android.support.design.widget.Snackbar.make(view, "Sem conexão com Internet. Por favor, verifique sey WiFi ou 3G.", android.support.design.widget.Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        startActivity(it);
                                    }
                                })
                                .setActionTextColor(getActivity().getResources().getColor( R.color.coloLink ))
                            .show();

                        /*SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .text("Sem conexão com Internet. Por favor, verifique sey WiFi ou 3G.")
                                        .color(getActivity().getResources().getColor(android.R.color.black))
                                        .textColor(getActivity().getResources().getColor(android.R.color.white))
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .type(SnackbarType.MULTI_LINE)
                                        .actionLabel("Conectar")
                                        .actionColor(getActivity().getResources().getColor(R.color.coloLink))
                                        .actionListener(new ActionClickListener() {
                                            @Override
                                            public void onActionClicked(Snackbar snackbar) {
                                                Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                                startActivity(it);
                                            }
                                        })
                                        .eventListener(new EventListenerAdapter() {
                                            @Override
                                            public void onShown(Snackbar snackbar) {
                                                super.onShown(snackbar);
                                                ObjectAnimator.ofFloat(fab, "translationY", -snackbar.getHeight()).start();
                                            }

                                            @Override
                                            public void onDismissed(Snackbar snackbar) {
                                                super.onDismissed(snackbar);
                                                ObjectAnimator.ofFloat(fab, "translationY", 0).start();
                                            }

                                            @Override
                                            public void onDismiss(Snackbar snackbar) {
                                                super.onDismiss(snackbar);
                                                ObjectAnimator.ofFloat(fab, "translationY", 0).start();
                                            }
                                        }), (ViewGroup) view
                        );*/

                    }

                }
            });


        return view;
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
                        .setActionTextColor(getActivity().getResources().getColor( R.color.coloLink ))
                        .show();
            }
        });
        /*fab = (FloatingActionMenu) view.findViewById(R.id.fab);
        fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean b) {
                Toast.makeText(getActivity(), "Is menu opened? "+(b ? "true" : "false"), Toast.LENGTH_SHORT).show();
            }
        });
        fab.showMenuButton(true);
        fab.setClosedOnTouchOutside(true);

        FloatingActionButton fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        FloatingActionButton fab4 = (FloatingActionButton) view.findViewById(R.id.fab4);
        FloatingActionButton fab5 = (FloatingActionButton) view.findViewById(R.id.fab5);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);*/

        /*fab = (ActionButton) getActivity().findViewById(R.id.fab);

        fab.setButtonColor(getActivity().getResources().getColor(R.color.colorFAB));
        fab.setButtonColorPressed(getActivity().getResources().getColor(R.color.colorFABPressed));

        fab.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        fab.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);

        fab.setImageResource( R.drawable.ic_plus );

        float scale = getActivity().getResources().getDisplayMetrics().density;
        int shadow = (int)(3 * scale + 0.5);
        fab.setShadowRadius(shadow);

        fab.setOnClickListener(this);
        fab.playShowAnimation();*/


        /*fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

            }
            @Override
            public void onScrollUp() {

            }
        }, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //GridLayoutManager llm = (GridLayoutManager) mRecyclerView.getLayoutManager();
                /*StaggeredGridLayoutManager llm = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                int[] aux = llm.findLastCompletelyVisibleItemPositions(null);
                int max = -1;
                for(int i = 0; i < aux.length; i++){
                    max = aux[i] > max ? aux[i] : max;
                }*/

                /*CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    //if (mList.size() == max + 1) {
                    List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });
        fab.setOnClickListener(this);*/
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




        /*CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
    }
    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): "+position, Toast.LENGTH_SHORT).show();

        /*CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
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
                                rv.getChildPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv) );
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
    public void onClick(View v) {
        String aux = "";

        /*switch( v.getId() ){
            case R.id.fab1:
                aux = "Fab 1 clicked";
                break;
            case R.id.fab2:
                aux = "Fab 2 clicked";
                break;
            case R.id.fab3:
                aux = "Fab 3 clicked";
                break;
            case R.id.fab4:
                aux = "Fab 4 clicked";
                break;
            case R.id.fab5:
                aux = "Fab 5 clicked";
                break;
        }*/

        Toast.makeText(getActivity(), aux, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mList", (ArrayList<Car>) mList);
    }
}
