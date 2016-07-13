package com.finatext.investgate.fragment.summary;


public class TradeHistoryFragmentStock extends TradeHistoryFragmentAstrack {

    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            callApiTradeSummary(page, "stock");
    }

}