package com.rentracks.matching.fragment.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.adapter.SearchAdapter;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;

import java.util.List;

public class SearchAbstractFragment extends AbstractPullAndLoadmoreFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    protected void loadDirection(boolean is_load_up) {

    }

    SearchAdapter.SearchItemAdapter mAdapter;

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
    }

    @Override
    public void SearchAction(String s) {
        super.SearchAction(s);
    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new SearchAdapter.SearchItemAdapter();
        }
        return mAdapter;
    }
    @Override
    public void onItemClick(View view, int position) {
//        startFragment(new EventDetailFragment(),true);
    }
    @Override
    protected void loadData(final int page) {

    }
    protected void callApiTradeSummary(final int page, final String type) {

//        demoData(page);
//        Observable<ListDto<TradeHistoryItem>> objectDtoObservable = matchingApi.searchEvent(page, type);
//        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDto<TradeHistoryItem>>(this.getActivity(), true) {
//            @Override
//            protected void onDataError(ListDto<TradeHistoryItem> tradeSummaryItemListDto) {
//                notifyLoadFail(page);
//            }
//
//            @Override
//            public void onDataSuccess(ListDto<TradeHistoryItem> tradeSummaryYearItem) {
//                if(tradeSummaryYearItem.data != null){
//                    List<TradeHistoryItem> items_history =tradeSummaryYearItem.data.items;
//                    List<EventSearchItem> list = convertData(items_history);
//                    int last_page = 1;
//                    int page = 1;
//                    if(tradeSummaryYearItem.data.pageInfo != null){
//                        last_page = tradeSummaryYearItem.data.pageInfo.lastPage;
//                        page = tradeSummaryYearItem.data.pageInfo.page;
//                    }
//                    showData(page, last_page, list);
//                }else {
//                    notifyLoadFail(page);
//                }
//            }
//        });
    }
//    private  List<EventSearchItem> convertData(List<TradeHistoryItem> items_history) {
//        List<EventSearchItem> items = new ArrayList<EventSearchItem>(items_history.size()) ;
//        int  day = 0;
//        for(int i = 0; i< items_history.size(); i++){
//            try {
//                EventSearchItem item = new EventSearchItem();
//                item.status = items_history.get(i).status;
//                item.name = items_history.get(i).name;
//                item.type = items_history.get(i).type;
//                item.companyname = items_history.get(i).company_name;
//                item.position_pl = items_history.get(i).TradeVol;
//                item.stockTradeId = items_history.get(i).stockTradeId;
//
//                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(items_history.get(i).datetime);
//                item.datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
//                item.year = new SimpleDateFormat("yyyy/MM/dd").format(date);
//                int day_format = Integer.valueOf(new SimpleDateFormat("dd").format(date));
//                if(day_format != day){
//                    item.isHeader = true;
//                    day = day_format;
//                }
//                items.add(item);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return items;
//    }
    private void showData(int page, int last_page, List<EventSearchItem> items){
        //step2 notify loaded data
        notifyLoaded(page, last_page, items);
        //step 3 add data to list
        if(items!=null) {
            mAdapter.addAll(items);
            notifyDataSetChanged();
        }else{
            notifyLoadFail(page);
        }
    }
    /*private void demoData(final int page){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<EventSearchItem> items = new ArrayList<>();
                if(page <=3) {
                    int year = 2016;
                    for (int i = 0; i < 20; i++) {
//                        items.add("item page " + page + "  order" + i);
                        EventSearchItem temp= new EventSearchItem();
                        temp.name="History"+i;
                        temp.position_pl= 435;
                        temp.datetime="2016/05/01 23:09";
                        temp.companyname="abgdfgdfgdf gdfgfgdc"+i;
//                        temp.status="ssss";
                        temp.type="xyz";
                        temp.isHeader = i%3 ==0;
                        temp.year = String.valueOf(year);
                        if(temp.isHeader){
                            year--;
                        }
                        items.add(temp);
                    }
                }
                //step2 notify loaded data
                notifyLoaded(page, 3, items);
                //step 3 add data to list
                if(items!=null) {
                    mAdapter.addAll(items);
                    notifyDataSetChanged();
                }
            }
        },1500);
        //TODO if load fail  notifyLoadFail(page);
    }
    */
}
