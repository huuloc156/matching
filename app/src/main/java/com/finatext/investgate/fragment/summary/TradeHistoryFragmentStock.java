package com.finatext.investgate.fragment.summary;


import android.view.View;

import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearItem;
import com.finatext.investgate.data.api.dto.summary.TradeDto;
import com.finatext.investgate.data.api.dto.summary.TradeEach;

import rx.Observable;

public class TradeHistoryFragmentStock extends TradeHistoryFragmentAstrack {

    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            callApiTradeSummary(page, "stock");
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        ProfitLossYearItem item = mAdapter.getItem(position);
        int id = 1463;
        Observable<ObjectDto<TradeDto<TradeEach>>> objectDtoObservable = investgateApi.getEachTrade(id);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<TradeDto<TradeEach>>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<TradeDto<TradeEach>> tradeSummaryItemListDto) {
                notifyLoadFail(1);
            }

            @Override
            public void onDataSuccess(ObjectDto<TradeDto<TradeEach>> Items) {
//                TradeEach item = Items.data.ValueData;
//                android.support.v4.app.Fragment fragment = new TradeHistoryDetailFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("MyData", item);
            }
        });
    }
}