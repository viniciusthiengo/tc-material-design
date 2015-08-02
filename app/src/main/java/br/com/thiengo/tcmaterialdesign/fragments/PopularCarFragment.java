package br.com.thiengo.tcmaterialdesign.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class PopularCarFragment extends CarFragment {


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

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1
                        && (mSwipeRefreshLayout == null || !mSwipeRefreshLayout.isRefreshing()) ) {
                    NetworkConnection.getInstance(getActivity()).execute(PopularCarFragment.this, PopularCarFragment.class.getName());
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        CarAdapter adapter = new CarAdapter(getActivity(), mList, true, false);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        activateSwipRefresh(view, this, PopularCarFragment.class.getName());

        setFloatingActionButton(view);

        return view;
    }


    public void callVolleyRequest(  ){
        NetworkConnection.getInstance(getActivity()).execute(this, PopularCarFragment.class.getName() );
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(PopularCarFragment.class.getName());
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
                car.setCategory(4);

                if( mList != null && mList.size() > 0 ){
                    car.setId( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ? mList.get(0).getId() : mList.get(mList.size() - 1).getId() );
                }

                return( new WrapObjToNetwork(car, "get-cars", (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) ) );
            }
            return null;
        }
}
