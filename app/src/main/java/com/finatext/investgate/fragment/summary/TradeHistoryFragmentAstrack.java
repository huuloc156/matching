package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finatext.investgate.R;
import com.finatext.investgate.adapter.RecyclerArrayAdapter;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ListDto;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearItem;
import com.finatext.investgate.data.api.dto.summary.TradeHistoryItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by RENTRACKS VN3 on 7/13/2016.
 */

public class TradeHistoryFragmentAstrack extends AbstractPullAndLoadmoreFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);
        return inflater.inflate(R.layout.fragment_summary_trade_history, container, false);
    }

    ProfitLossTabYearFragment.ProfitLossYearItemAdapter mAdapter;

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ProfitLossTabYearFragment.ProfitLossYearItemAdapter();
        }
        return mAdapter;
    }
    @Override
    public void onItemClick(View view, int position) {
        startFragment(new TradeHistoryDetailFragment(),true);
    }
    @Override
    protected void loadData(final int page) {

    }
    public void callApiTradeSummary(final int page, final String type) {

        Observable<ListDto<TradeHistoryItem>> objectDtoObservable = investgateApi.getDailyTradeList(page, type);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDto<TradeHistoryItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDto<TradeHistoryItem> tradeSummaryItemListDto) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDto<TradeHistoryItem> tradeSummaryYearItem) {
                List<TradeHistoryItem> items_history =tradeSummaryYearItem.data.items;
                List<ProfitLossYearItem> list = convertData(items_history);
//                if(type == "stock") {
//                    listStock = list;
//                }
                int last_page = 1;
                int page = 1;
                showData(page, last_page, list);
            }
        });
    }
    private  List<ProfitLossYearItem> convertData( List<TradeHistoryItem> items_history) {
        List<ProfitLossYearItem> items = new ArrayList<ProfitLossYearItem>(items_history.size()) ;
        int  year = 0;
        for(int i = 0; i< items_history.size(); i++){
            try {
                ProfitLossYearItem item = new ProfitLossYearItem();
                item.status = items_history.get(i).status;
                item.name = items_history.get(i).name;
                item.type = items_history.get(i).type;
                item.companyname = items_history.get(i).company_name;
                item.position_pl = items_history.get(i).TradeVol;

                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(items_history.get(i).datetime);
                item.datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
                item.year = new SimpleDateFormat("yyyy/MM/dd").format(date);
                int year_format = Integer.valueOf(new SimpleDateFormat("yyyy").format(date));
                if(year_format != year){
                    item.isHeader = true;
                    year = year_format;
                }
                items.add(item);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
    private void showData(int page, int last_page, List<ProfitLossYearItem> items){
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
    private void demoData(final int page){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossYearItem> items = new ArrayList<>();
                if(page <=3) {
                    int year = 2016;
                    for (int i = 0; i < 20; i++) {
//                        items.add("item page " + page + "  order" + i);
                        ProfitLossYearItem temp= new ProfitLossYearItem();
                        temp.name="History"+i;
                        temp.position_pl="+435...";
                        temp.datetime="2016/05/01 23:09";
                        temp.companyname="abgdfgdfgdf gdfgfgdc"+i;
                        temp.status="ssss";
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
}
