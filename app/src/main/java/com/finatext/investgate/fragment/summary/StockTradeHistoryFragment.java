package com.finatext.investgate.fragment.summary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.summary.TradeEach;
import com.finatext.investgate.utils.ToastUtils;

import rx.Observable;

public class StockTradeHistoryFragment extends TradeHistoryFragment {

    public static Fragment getInstance(){
        return new StockTradeHistoryFragment();
    }
    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            callApiTradeSummary(page, "stock");
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        setRefreshing(true);
        int id =  mAdapter.getItem(position).stockTradeId;
        Observable<ObjectDto<TradeEach>> objectDtoObservable = investgateApi.getEachTrade(id);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<TradeEach>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<TradeEach> tradeSummaryItemListDto) {
                setRefreshing(false);
            }

            @Override
            public void onDataSuccess(ObjectDto<TradeEach> Items) {
                setRefreshing(false);
                if(Items.data == null){
                    ToastUtils.show(getContext(), "Null");
                    return;
                }
                TradeEach item = Items.data;
//                TradeEach item = new TradeEach();
//                item.commission_fee = 789;
//                item.type = "投信";
//                item.name = "日産自動車";
//                item.date = "dddd";
//                item.trading_volumne = 123;
//                item.interest = 456;

                android.support.v4.app.Fragment fragment = new TradeHistoryDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("TradeEachData", item);
                fragment.setArguments(bundle);
                startFragment(fragment,true);
            }
        });
    }
}