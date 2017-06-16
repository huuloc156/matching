package com.rentracks.matching.fragment.timeline;


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
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.myaccount.AccountFragment;
import com.rentracks.matching.listener.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class SearchUserHomeFragment extends SearchAbstractFragment implements ListenerClose {

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
        bundle.putString("tab_filter", "user");
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
        return new SearchUserHomeFragment();
    }

    SearchUserItemAdapter mAdapter;


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
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new SearchUserItemAdapter();
        }
        return mAdapter;
    }


    @Override
    protected void loadData(final int page) {
        setUILoading(page);
        int distance = 20;
        int limit = 10;
        int age_from = 1;
        int age_to = 40;
        int gender = -1;
        String filter = preferenceData.getUserFilter("20#Both#18#30");
        List<String> element = Arrays.asList(filter.split("#"));
        if(element.size() > 3){
            distance = Integer.valueOf(element.get(0));
            if(element.get(1).equals(getResources().getStringArray(R.array.gender_full)[0])){//both
                gender = -1;
            }else  if(element.get(1).equals(getResources().getStringArray(R.array.gender_full)[1])){//Male
                gender = 0;
            }else{
                gender = 1;//female
            }
            age_from = Integer.valueOf(element.get(2));
            age_to = Integer.valueOf(element.get(3));
        }
        callApiTradeSummary(page, distance, limit, age_from, age_to, gender, keySearch);
    }



    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        UserItem item = mAdapter.getItem(position);
        android.support.v4.app.Fragment fragment = new AccountFragment();
        Bundle bundle = new Bundle();
        item.isOwner = false;
        bundle.putParcelable("user_detail", item);
        fragment.setArguments(bundle);
        startFragment(fragment,true);
    }

    @Override
    public void close(Object listOfObjects) {
        boolean isChange = (boolean)listOfObjects;
        if(isChange == true) {
//            showMessToast("load new");
            loadData(1);
        }else{
//            showMessToast("no change");
        }
    }

    @Override
    public void clsee2(Object Objects) {

    }

    protected void callApiTradeSummary(final int page,
                                       final int distance,
                                       final int limit,
                                       final int age_from,
                                       final int age_to,
                                       final int gender,
                                       final String search) {

        Observable<ListDtoData<UserItem>> objectDtoObservable = matchingApi.searchUser(page, distance, limit, age_from, age_to, gender, search);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<UserItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<UserItem> events) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDtoData<UserItem> events) {
                if(events.data != null){
                    List<UserItem> items = events.data;
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


    public static class SearchUserItemAdapter extends RecyclerArrayAdapter<UserItem, SearchEventHomeFragment.SearchItemViewHolder> {
        boolean notshowArrow = false;
        public void setNotshowArrow(boolean is){
            notshowArrow = is;
        }
        @Override
        public SearchEventHomeFragment.SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_timeline, parent, false);
            return new SearchEventHomeFragment.SearchItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(final SearchEventHomeFragment.SearchItemViewHolder holder, int position) {
            UserItem item = getItem(position);
            if(item.name == null){
                holder.txtTitle.setText(item.email);
            }else {
                holder.txtTitle.setText(item.name);
            }
            int avt_size = R.dimen.avatar_user_size;
            String gender = holder.txtDescription.getContext().getResources().getStringArray(R.array.gender)[item.gender];
            if(notshowArrow == true){
                holder.imgArrow.setVisibility(View.GONE);
                avt_size = R.dimen.avatar_user_small_size;
                holder.txtDescription.setVisibility(View.GONE);
                holder.img_avt.getLayoutParams().height = 80;
                holder.img_avt.getLayoutParams().width = 80;
            }

            holder.txtDescription.setText(gender + "\n" + item.location);
            String picUrl = CommonUtils.getFullPicUrl(holder.img_avt.getContext(), item.getPic());
            LoadImageUtils.load(holder.img_avt.getContext(), picUrl)
                    .error(R.mipmap.noimage)
                    .placeholder(R.drawable.bg_circle)
                    .resizeDimen(avt_size, avt_size)
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


}