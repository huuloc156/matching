package com.rentracks.matching.fragment.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.adapter.RecyclerArrayViewHolder;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.search.PlaceItem;
import com.rentracks.matching.data.api.dto.search.PlaceMapItem;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by HuuLoc on 5/30/17.
 */

public class MapEventPlace extends AbstractPullAndLoadmoreFragment {
    @BindView(R.id.ll_suggess_search)
    ListView llSugges;
    String key_search;
    PlaceMapAdapter mAdapter;
    ListenerClose mListener;


    public static MapEventPlace getInstance(){
        return new MapEventPlace();
    }

    public void setListenerClose(ListenerClose l){
        mListener = l;
    }
    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setDivider(false);
        return inflater.inflate(R.layout.fragment_map_event_place, container, false);
    }
    @Override
    public String getSearchKeyword() {
        return key_search;
    }
    @Override
    public void SearchAction(String s) {
        super.SearchAction(s);
        key_search = s;
        loadData(1);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkHeader();
        createSuggestSearch();
        showSuggestSearch();
    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new PlaceMapAdapter();
        }
        return mAdapter;
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        mListener.close(mAdapter.getItem(position));
        goBack();
    }

    @Override
    protected void loadData(int page) {
        hideSuggestSearch();
        if(key_search == null){
            return;
        }
        callApiTradeSummary(key_search, page);
    }

    protected void callApiTradeSummary(String query, final int page) {
        String location = preferenceData.getLocationUser();
        int radius = 1000;
//        String type = "restaurant";
        String key = CommonUtils.getApiGoolgeSearch();

        setUILoading(page);

        Observable<PlaceMapItem<PlaceItem>> objectDtoObservable = matchingApi.searchPlace(query, /*type,*/ location, radius,key);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<PlaceMapItem<PlaceItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(PlaceMapItem<PlaceItem> data) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(PlaceMapItem<PlaceItem> data) {
                notifyLoaded(page, page+1, data.results);

                if(data.results != null){
                    mAdapter.addAll(data.results);
                    notifyDataSetChanged();
                }

            }
        });
    }
    private void hideSuggestSearch(){
        llSugges.setVisibility(View.GONE);
    }
    private void showSuggestSearch(){
        llSugges.setVisibility(View.VISIBLE);

    }
    private void createSuggestSearch(){
        String[] sug = getResources().getStringArray(R.array.Suggest_search);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, sug);
        // Assign adapter to ListView
        llSugges.setAdapter(adapter);
        llSugges.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                key_search    = (String) llSugges.getItemAtPosition(position);
                checkHeader();
                loadData(1);
            }

        });
    }



    public static class PlaceMapAdapter extends RecyclerArrayAdapter<PlaceItem, PlaceMapViewHolder> {
        @Override
        public PlaceMapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_timeline, parent, false);
            return new PlaceMapViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(final PlaceMapViewHolder holder, int position) {
            PlaceItem item = getItem(position);
            holder.txtTitle.setText(item.name);
            holder.txtDescription.setText(item.formatted_address);

            String picUrl = item.getPic(200);
            LoadImageUtils.load(holder.img_avt.getContext(), picUrl)
                    .error(R.mipmap.noimage)
                    .placeholder(R.drawable.bg_circle)
                    .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
                    .centerCrop()
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

            holder.imgArrow.setVisibility(View.GONE);

        }


    }

    public static class PlaceMapViewHolder extends RecyclerArrayViewHolder {

        @BindView(R.id.img_rit)
        ImageView img_avt;
        @BindView(R.id.txt_top_rit)
        TextView txtTitle;
        @BindView(R.id.txt_bottom_rit)
        TextView txtDescription;
        @BindView(R.id.progress_rit)
        ProgressBar progressBar;
        @BindView(R.id.img_arrow_forward_rit)
        ImageView imgArrow;

        public PlaceMapViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }
}
