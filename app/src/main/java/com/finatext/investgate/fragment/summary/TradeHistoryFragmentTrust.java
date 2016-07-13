package com.finatext.investgate.fragment.summary;

/**
 * Created by RENTRACKS VN3 on 7/13/2016.
 */

public class TradeHistoryFragmentTrust extends TradeHistoryFragmentAstrack {

    @Override
    protected void loadData(final int page) {
        setUILoading(page);
        callApiTradeSummary(page, "trust");
    }
}
