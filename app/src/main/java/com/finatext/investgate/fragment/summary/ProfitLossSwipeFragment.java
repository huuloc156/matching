package com.finatext.investgate.fragment.summary;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.AbstractSwipeFragment;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossSwipeFragment extends AbstractSwipeFragment {
    @Override
    protected void initPagerFragment() {
        mCustomHeaderText = getResources().getString(R.string.header_profit_loss);
        addFragment("実現損益", new ProfitLossTabCurrentFragment());
        addFragment("年間の累計損益", new ProfitLossTabYearFragment());
    }


    @Override
    public int getHeaderRightButtonImageResId() {
        return R.mipmap.header_reload;
    }



}
