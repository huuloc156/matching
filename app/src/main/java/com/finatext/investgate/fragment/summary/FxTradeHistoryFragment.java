package com.finatext.investgate.fragment.summary;

import android.support.v4.app.Fragment;

public class FxTradeHistoryFragment extends TradeHistoryFragment {

    public static Fragment getInstance(){
        return new FxTradeHistoryFragment();
    }
    @Override
    protected void loadData(final int page) {
//        setUILoading(page);
//        callApiTradeSummary(page, "fx");
    }
}
