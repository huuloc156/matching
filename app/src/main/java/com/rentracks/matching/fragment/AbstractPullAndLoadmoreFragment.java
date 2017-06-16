package com.rentracks.matching.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.DividerDecoration;
import com.rentracks.matching.adapter.EndlessRecyclerOnScrollListener;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.adapter.RecyclerArrayViewHolder;
import com.rentracks.matching.adapter.RecyclerViewHeaderFooterAdapter;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public abstract class AbstractPullAndLoadmoreFragment extends AbstractPullToRefreshFragment implements
        RecyclerArrayViewHolder.OnItemRecyclerClick,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;
    private RecyclerViewHeaderFooterAdapter mRecyclerViewHeaderFooterAdapter;
    private View mFooterLoadMore;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    protected boolean isGrid;
    protected boolean isHaveDivider= true;
    private  boolean isLoadUpDown = false;
    protected boolean isReversScroll = false;

    public AbstractPullAndLoadmoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pull_to_refresh_load_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set adapter populated with example dummy data
        LinearLayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
            if(isHaveDivider()) {
                mRecyclerView.addItemDecoration(new DividerDecoration(getActivity()));
            }
        }
        if(isReversScroll) {
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
        }

        if(mRecyclerViewHeaderFooterAdapter == null) {
            mRecyclerViewHeaderFooterAdapter = new RecyclerViewHeaderFooterAdapter(layoutManager,createAdapter());
        }
        mRecyclerViewHeaderFooterAdapter.setLayoutManager(layoutManager);
        if(mFooterLoadMore == null){
            mFooterLoadMore = LayoutInflater.from(getActivity()).inflate(R.layout.include_footer_loadmore,null);
            hideFooter();
        }
        TextView tvEmpty = (TextView) mFooterLoadMore.findViewById(R.id.tv_empty_message);
        tvEmpty.setText(getEmptyListMessage());
        mRecyclerViewHeaderFooterAdapter.getSubAdapter().setOnItemRecyclerClick(this);
        mRecyclerViewHeaderFooterAdapter.addFooter(mFooterLoadMore);
        mRecyclerView.setAdapter(mRecyclerViewHeaderFooterAdapter);
        // Set layout manager
        mRecyclerView.setLayoutManager(layoutManager);
        if(endlessRecyclerOnScrollListener == null) {
            endlessRecyclerOnScrollListener = endlessListener(layoutManager);
        }else{
            endlessRecyclerOnScrollListener.setLayoutManager(layoutManager);
        }
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        if( isFirstCreate){
            loadDirection(isLoadUpDown);
            loadData(1);
        }
    }
    protected int getEmptyListMessage() {
        return R.string.empty_message;
    }

    protected int getSpanCount() {
        return 3;
    }

    protected  void setDivider(boolean ishavedivider){
        isHaveDivider = ishavedivider;
    }
    protected boolean isHaveDivider() {
        return isHaveDivider;
    }

    @NonNull
    protected EndlessRecyclerOnScrollListener endlessListener(final LinearLayoutManager layoutManager) {

        return new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    isLoadUpDown = true;
                } else {
                    // Scrolling down
                    isLoadUpDown = false;
                }
            }

            @Override
            public void onLoadMore(final int current_page) {
                // do something...
                Timber.tag("EndlessRecyclerOnScrollListener").i( "onloadmore " + current_page);
                loadDirection(isLoadUpDown);
                loadData(current_page);

            }
        };
    }
    protected abstract void loadDirection(boolean is_load_up);

    protected abstract RecyclerArrayAdapter createAdapter();

    @Override
    public void onRefresh() {
        loadDirection(isLoadUpDown);
        loadData(1);
    }
    public void notifyDataSetChanged(){
        if(mRecyclerViewHeaderFooterAdapter!=null) {
            mRecyclerViewHeaderFooterAdapter.notifyDataSetChanged();
        }
    }
    protected abstract void loadData(int page);

    /**
     * Call before add data to adapter
     * @param page
     */
    public void notifyLoaded(int page){
        if(page == 1){
            mRecyclerViewHeaderFooterAdapter.getSubAdapter().clear();
            mRefreshLayout.setRefreshing(false);
        }
        endlessRecyclerOnScrollListener.setLoaded();
    }
    /**
     * importance
     * Call before add data to adapter
     * @param page
     */
    public void notifyLoaded(int page,int lastPage,List items){
        boolean isEmpty = false;
        TextView tvEmpty = (TextView) mFooterLoadMore.findViewById(R.id.tv_empty_message);
        if(page == 1){
            mRecyclerViewHeaderFooterAdapter.getSubAdapter().clear();
            mRefreshLayout.setRefreshing(false);
            if(((items == null || items.isEmpty()) && isCheckEmpty())){
                //show empty message
                tvEmpty.setVisibility(View.VISIBLE);
                mFooterLoadMore.findViewById(R.id.img_end_list).setVisibility(View.GONE);
            }else{
                tvEmpty.setVisibility(View.GONE);
                if(lastPage == 1){
                    //1 page and end load more
                    showEndList();
                }
            }
        }else {
            tvEmpty.setVisibility(View.INVISIBLE);
        }
        if(page <lastPage) {
            endlessRecyclerOnScrollListener.setLoaded();
            mFooterLoadMore.findViewById(R.id.img_end_list).setVisibility(View.GONE);
        }else {
            hideFooter();
            //end page
            if(!isGrid && page!=1) {
                showEndList();
            }
        }
    }

    protected void showEndList() {
        if(isShowEndList()) {
            mFooterLoadMore.findViewById(R.id.img_end_list).setVisibility(View.VISIBLE);
        }else{
            mFooterLoadMore.findViewById(R.id.img_end_list).setVisibility(View.INVISIBLE);
        }
    }

    protected boolean isShowEndList() {
        return false;
    }

    protected boolean isCheckEmpty() {
        return true;
    }

    public void notifyLoadFail(int page){
        mRefreshLayout.setRefreshing(false);
        //TODO load fail
        hideFooter();
    }
    public void setUILoading(int page){
        if(page == 1){
            endlessRecyclerOnScrollListener.resetPage();
            setRefreshing(true);
            hideFooter();
        }else{
            mRefreshLayout.setRefreshing(false);
            showFooter();
        }
    }
    protected void hideFooter() {
        mFooterLoadMore.findViewById(R.id.progressBarLoadmore).setVisibility(View.INVISIBLE);
    }
    protected void showFooter() {
        mFooterLoadMore.findViewById(R.id.progressBarLoadmore).setVisibility(View.VISIBLE);
        mFooterLoadMore.findViewById(R.id.img_end_list).setVisibility(View.GONE);
        mFooterLoadMore.findViewById(R.id.tv_empty_message).setVisibility(View.GONE);
    }

    public void setRefreshing(final boolean refreshing) {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(refreshing);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
