package com.finatext.investgate.fragment.summary;

import com.finatext.investgate.fragment.AbstrackSwipeFragmentCustom;

/**
 * Created by apple on 6/20/16.
 */
public class TradeHistoryFragment extends AbstrackSwipeFragmentCustom {

    @Override
    protected void initPagerFragment() {
        addFragment("A", new TradeHistoryFragmentStock());
        addFragment("B", new TradeHistoryFragmentFx());
        addFragment("C", new TradeHistoryFragmentTrust());
    }
}
