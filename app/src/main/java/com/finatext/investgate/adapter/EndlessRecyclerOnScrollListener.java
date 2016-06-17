package com.finatext.investgate.adapter;

/**
 * Created by apple on 9/12/15.
 */
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = mLinearLayoutManager.getItemCount();
        int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        int visibleThreshold = 2;
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }
    public void setLoaded(){
        loading = false;
    }

    public abstract void onLoadMore(int current_page);

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.mLinearLayoutManager = layoutManager;
    }


    public void resetPage() {
        current_page = 1;
    }
}
