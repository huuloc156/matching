package com.finatext.investgate.fragment.summary;

import com.finatext.investgate.fragment.CustomAbstractSwipeFragment;

/**
 * Created by apple on 6/20/16.
 */
public class TradeHistoryFragmentAbstractSwipeFragment extends CustomAbstractSwipeFragment {

    @Override
    protected void initPagerFragment() {
        addFragment("株式", new TradeHistoryFragmentStock());
        addFragment("FX", new TradeHistoryFragmentFx());
        addFragment("投信", new TradeHistoryFragmentTrust());
    }
}
