package com.rentracks.matching.fragment.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.timeline.EventDetailFragment;
import com.rentracks.matching.fragment.timeline.SearchEventHomeFragment;
import com.rentracks.matching.listener.ListenerClose;

import java.util.List;

import rx.Observable;

/**
 * Created by HuuLoc on 6/6/17.
 */

public class AbstractScheduleFragment extends AbstractPullAndLoadmoreFragment implements ListenerClose {


    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);

        MakingEventFragment fragment = (MakingEventFragment) MakingEventFragment.getInstance();
        Bundle bundle = new Bundle();
//        bundle.putString("tab_filter", "event");
        fragment.setArguments(bundle);
//        fragment.setListenerClose(this);
        startFragment(fragment, true);
    }



    SearchEventHomeFragment.SearchEventItemAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkHeader();
    }

    @Override
    protected void loadDirection(boolean is_load_up) {

    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if (mAdapter == null) {
            mAdapter = new SearchEventHomeFragment.SearchEventItemAdapter();
        }
        return mAdapter;
    }


    @Override
    protected void loadData(final int page) {
        setUILoading(page);
        int limit = 10;
//            SearchEventHomeFragment.callApiTradeSummary(page, distance, limit, keySearch);
        Observable<ListDtoData<EventSearchItem>> objectDtoObservable = matchingApi.futureEvent(page, limit);
        callApiTradeSummary(objectDtoObservable, page, limit);
    }


    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        EventSearchItem item = mAdapter.getItem(position);
        Fragment fragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event_detail", item);
        fragment.setArguments(bundle);
        startFragment(fragment, true);
    }

    @Override
    public void close(Object listOfObjects) {
        loadData(1);
    }

    @Override
    public void clsee2(Object Objects) {

    }

    public void callApiTradeSummary(Observable<ListDtoData<EventSearchItem>> objectDtoObservable, final int page, final int limit) {


        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<EventSearchItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<EventSearchItem> events) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDtoData<EventSearchItem> events) {
                if(events.data != null){
                    List<EventSearchItem> items = events.data;
                    int last_page = page + 1;
                    if(items.size() < limit){
                        last_page = page;
                    }
                    //step2 notify loaded data
                    notifyLoaded(page, last_page, items);
                    //step 3 add data to list
                    if(items!=null) {
                        mAdapter.addAll(items);
                        notifyDataSetChanged();
                    }else{
                        notifyLoadFail(page);
                    }

                }else {
                    notifyLoadFail(page);
                }
            }
        });
    }
}
