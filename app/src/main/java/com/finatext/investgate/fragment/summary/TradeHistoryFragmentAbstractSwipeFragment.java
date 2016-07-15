package com.finatext.investgate.fragment.summary;

import com.finatext.investgate.fragment.CustomAbstractSwipeFragment;

/**
 * Created by apple on 6/20/16.
 */
public class TradeHistoryFragmentAbstractSwipeFragment extends CustomAbstractSwipeFragment {

    @Override
    protected void initPagerFragment() {
        addFragment("A", new TradeHistoryFragmentStock());
        addFragment("B", new TradeHistoryFragmentFx());
        addFragment("C", new TradeHistoryFragmentTrust());
    }
}
