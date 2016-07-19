package com.finatext.investgate.fragment.summary;

import android.support.v4.app.Fragment;
import android.view.View;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.AbstractSwipeFragment;

import timber.log.Timber;

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

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        Timber.tag("prfitloss").i("onClickHeader");
    }



    public static Fragment getInstance() {
        return new ProfitLossSwipeFragment();
    }
}
