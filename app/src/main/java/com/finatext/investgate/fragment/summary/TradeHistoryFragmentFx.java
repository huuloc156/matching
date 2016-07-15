package com.finatext.investgate.fragment.summary;

public class TradeHistoryFragmentFx extends TradeHistoryFragmentAstrack {

    @Override
    protected void loadData(final int page) {
        setUILoading(page);
        callApiTradeSummary(page, "fx");
    }
}
