package br.com.thiengo.tcmaterialdesign.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.adapters.CarAdapter;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;
import br.com.thiengo.tcmaterialdesign.extras.UtilTCM;
import br.com.thiengo.tcmaterialdesign.network.NetworkConnection;

/*import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;*/

public class OldCarFragment extends CarFragment{

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);

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

                StaggeredGridLayoutManager llm = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                int[] aux = llm.findLastCompletelyVisibleItemPositions(null);
                int max = -1;
                for (int i = 0; i < aux.length; i++) {
                    max = aux[i] > max ? aux[i] : max;
                }

                if (mList.size() == max + 1
                        && (mSwipeRefreshLayout == null || !mSwipeRefreshLayout.isRefreshing()) ) {
                    NetworkConnection.getInstance(getActivity()).execute(OldCarFragment.this, OldCarFragment.class.getName());
                }
            }
        });

        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        llm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(llm);

        CarAdapter adapter = new CarAdapter(getActivity(), mList, false, true);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        activateSwipRefresh(view, this, OldCarFragment.class.getName());

        setFloatingActionButton(view);

        return view;
    }


    public void callVolleyRequest(  ){
        NetworkConnection.getInstance(getActivity()).execute(this, OldCarFragment.class.getName() );
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(OldCarFragment.class.getName());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // NETWORK
        @Override
        public WrapObjToNetwork doBefore() {
            mPbLoad.setVisibility(View.VISIBLE);

            if( UtilTCM.verifyConnection(getActivity()) ){
                Car car = new Car();
                car.setCategory(3);

                if( mList != null && mList.size() > 0 ){
                    car.setId( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ? mList.get(0).getId() : mList.get(mList.size() - 1).getId() );
                }

                return( new WrapObjToNetwork(car, "get-cars", (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) ) );
            }
            return null;
        }
}
