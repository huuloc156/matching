package com.rentracks.matching.fragment.timeline;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.adapter.RecyclerArrayViewHolder;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.listener.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class SearchEventHomeFragment extends AbstractPullAndLoadmoreFragment implements ListenerClose {

    String keySearch = "";

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    protected boolean isShowEndList() {
        return false;
    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);

        FilterSearchFragment fragment = (FilterSearchFragment) FilterSearchFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("tab_filter", "event");
        fragment.setArguments(bundle);
        fragment.setListenerClose(this);
        startFragment(fragment,true);
    }


    @Override
    public void SearchAction(String s) {
        super.SearchAction(s);
        keySearch = s;
        if(s != null) {
            loadData(1);
        }
    }

    @Override
    public String getSearchKeyword() {
        return keySearch;
    }

    public static Fragment getInstance(){
        return new SearchEventHomeFragment();
    }

    SearchEventItemAdapter mAdapter;


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
        checkSettingAccount();
    }

    @Override
    protected void loadDirection(boolean is_load_up) {
        Log.i("", "loadDireaction "+is_load_up);
    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new SearchEventItemAdapter();
        }
        return mAdapter;
    }


    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            int distance = 10;
            int limit = 10;
            Observable<ListDtoData<EventSearchItem>> objectDtoObservable = matchingApi.searchEvent(page, distance, limit, keySearch);
            callApiTradeSummary(objectDtoObservable, page, limit);
    }



    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
            EventSearchItem item = mAdapter.getItem(position);
            android.support.v4.app.Fragment fragment = new EventDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("event_detail", item);
            fragment.setArguments(bundle);
            startFragment(fragment,true);
}

    @Override
    public void close(Object listOfObjects) {
        boolean isChange = (boolean)listOfObjects;
        if(isChange == true) {
            loadData(1);
        }
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


    public static class SearchEventItemAdapter extends RecyclerArrayAdapter<EventSearchItem, SearchItemViewHolder> {
        @Override
        public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_timeline, parent, false);
            return new SearchItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(final SearchItemViewHolder holder, int position) {
            EventSearchItem item = getItem(position);
            holder.txtTitle.setText(item.title);
            holder.txtDescription.setText(item.description);
            String picUrl = CommonUtils.getFullPicUrl(holder.img_avt.getContext(), item.getPic());
            LoadImageUtils.load(holder.img_avt.getContext(), picUrl)
                    .error(R.mipmap.noimage)
                    .placeholder(R.drawable.bg_circle)
                    .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
                    .centerCrop()
//                    .centerInside()
                    .into(holder.img_avt,new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });

        }


    }

    public static class SearchItemViewHolder extends RecyclerArrayViewHolder {

        @BindView(R.id.img_rit)
        public  ImageView img_avt;
        @BindView(R.id.txt_top_rit)
        public TextView txtTitle;
        @BindView(R.id.txt_bottom_rit)
        public TextView txtDescription;
        @BindView(R.id.progress_rit)
        public ProgressBar progressBar;
        @BindView(R.id.img_arrow_forward_rit)
        ImageView imgArrow;

        public SearchItemViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }
    public void checkSettingAccount(){
        if(preferenceData.getUserName().equals(null) ||
                preferenceData.getUserName().equals("")){
            selectTab(3);
        }
    }
}