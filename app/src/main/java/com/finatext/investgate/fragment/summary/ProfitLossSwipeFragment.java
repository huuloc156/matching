package com.finatext.investgate.fragment.summary;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.AbstractSwipeFragment;
import com.finatext.investgate.fragment.header.IHeaderInfo;

import timber.log.Timber;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossSwipeFragment extends AbstractSwipeFragment implements ViewPager.OnPageChangeListener {

    @Override
    protected void initPagerFragment() {
        mCustomHeaderText = getResources().getString(R.string.header_profit_loss);
        addFragment("実現損益", new ProfitLossTabCurrentFragment());
        addFragment("年間の累計損益", new ProfitLossTabYearFragment());
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        if(mViewPager!=null && mSectionsPagerAdapter!=null && mSectionsPagerAdapter.getCount()>0){
            int currentItem = mViewPager.getCurrentItem();
            if(currentItem < mSectionsPagerAdapter.getCount()){
                Object instantiateItem = mSectionsPagerAdapter.getItem(currentItem);
                if(instantiateItem instanceof IHeaderInfo){
                    return ((IHeaderInfo) instantiateItem).getHeaderRightButtonImageResId();
                }

            }
        }
        return 0;
    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        Timber.tag("prfitloss").i("onClickHeader");
        if(mViewPager!=null && mSectionsPagerAdapter!=null){
            Object instantiateItem = mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            if(instantiateItem instanceof IHeaderInfo){
                ((IHeaderInfo) instantiateItem).onClickHeaderRightButton(view);
            }
        }
    }



    public static Fragment getInstance() {
        return new ProfitLossSwipeFragment();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        checkHeader();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
