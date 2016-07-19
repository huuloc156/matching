package com.finatext.investgate.fragment.summary;

import android.support.v4.app.Fragment;

import com.finatext.investgate.fragment.CustomAbstractSwipeFragment;

/**
 * Created by apple on 6/20/16.
 */
public class CustomTradeHistoryFragment extends CustomAbstractSwipeFragment {

    @Override
    protected void initPagerFragment() {
        addFragment("株式", StockTradeHistoryFragment.getInstance());
        addFragment("FX", FxTradeHistoryFragment.getInstance());
        addFragment("投信", TrustTradeHistoryFragment.getInstance());
    }
    public static Fragment getInstance(){
        return new CustomTradeHistoryFragment();
    }
}
