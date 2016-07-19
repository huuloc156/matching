package com.finatext.investgate.fragment.summary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearItem;
import com.finatext.investgate.data.api.dto.summary.TradeDto;
import com.finatext.investgate.data.api.dto.summary.TradeEach;

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

        ProfitLossYearItem item = mAdapter.getItem(position);
        int id = 1463;
        Observable<ObjectDto<TradeDto<TradeEach>>> objectDtoObservable = investgateApi.getEachTrade(id);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<TradeDto<TradeEach>>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<TradeDto<TradeEach>> tradeSummaryItemListDto) {
                setRefreshing(false);
            }

            @Override
            public void onDataSuccess(ObjectDto<TradeDto<TradeEach>> Items) {
                setRefreshing(false);
//                TradeEach item = Items.data.valueData;
                TradeEach item = new TradeEach();
                item.commission_fee = 789;
                item.type = "投信";
                item.name = "日産自動車";
                item.date = "dddd";
                item.trading_volumne = 123;
                item.interest = 456;

                android.support.v4.app.Fragment fragment = new TradeHistoryDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("TradeEachData", item);
                fragment.setArguments(bundle);
                startFragment(fragment,true);
            }
        });
    }
}