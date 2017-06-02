package com.rentracks.matching.fragment.timeline;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rentracks.matching.R;
import com.rentracks.matching.fragment.CustomAbstractSwipeFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;

import timber.log.Timber;

/**
 * Created by apple on 6/20/16.
 */
public class HomeFragment extends CustomAbstractSwipeFragment implements ViewPager.OnPageChangeListener {


    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    protected void initPagerFragment() {
        mCustomHeaderText = getString(R.string.app_name);
        addFragment("Events", SearchEventHomeFragment.getInstance());
        addFragment("Near By User", SearchUserHomeFragment.getInstance());

        mViewPager.addOnPageChangeListener(this);

  }
    public static Fragment getInstance(){
        return new HomeFragment();
    }


    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        Timber.tag("HomeFragment").i("onClickHeader");
        if(mViewPager!=null && mSectionsPagerAdapter!=null){
            Object instantiateItem = mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            if(instantiateItem instanceof IHeaderInfo){
                ((IHeaderInfo) instantiateItem).onClickHeaderRightButton(view);
            }
        }
    }

    String search = "";
    @Override
    public String getSearchKeyword() {
        return search;
    }

    @Override
    public String saveSearchKeyword(String s) {
        search = s;
        return super.saveSearchKeyword(s);
    }

    @Override
    public void SearchAction(String s) {
        search = s;
        if(mViewPager!=null && mSectionsPagerAdapter!=null){
            Object instantiateItem = mSectionsPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            if(instantiateItem instanceof IHeaderInfo){
                ((IHeaderInfo) instantiateItem).SearchAction(s);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
