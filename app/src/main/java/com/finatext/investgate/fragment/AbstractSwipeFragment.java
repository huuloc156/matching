package com.finatext.investgate.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.finatext.investgate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractSwipeFragment extends BaseFragment {
    @BindView(R.id.pager_swipe)
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    boolean isTabbarRound = false;

    public AbstractSwipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_swipe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
            initPagerFragment();
        }

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                // fix tablayout is missing after second onViewCreated
                if(isAdded()) {
                    setupTabLayout();
                }
            }
        });
    }

    protected abstract void initPagerFragment() ;
    public void addFragment(String title,Fragment fragment){
        mSectionsPagerAdapter.addFragment(title,fragment);
    }

    protected void setupTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(mViewPager));
        if(mTabLayout.getSelectedTabPosition() != mViewPager.getCurrentItem()) {
            TabLayout.Tab tab = mTabLayout.getTabAt(mViewPager.getCurrentItem());
            if (tab != null) {
                tab.select();
            }
        }
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(Color.WHITE, getResources().getColor(R.color.bg_tab));
//        if(isTabbarRound) {
//            if (mTabLayout.getTabCount() >= 2) {
//                if(mTabLayout.getChildCount()>0 && (mTabLayout.getChildAt(0) instanceof ViewGroup)) {
//                    ViewGroup viewGroup = (ViewGroup) mTabLayout.getChildAt(0);
//                    if(viewGroup.getChildCount()>=2) {
//                        int lastChildIdx = viewGroup.getChildCount() - 1;
//                        viewGroup.getChildAt(lastChildIdx).setBackgroundResource(R.drawable.bar_bg_right_selector);
//                        viewGroup.getChildAt(0).setBackgroundResource(R.drawable.bar_bg_left_selector);;
//                    }
//                }
//            }
//        }
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
            if(tabAt!=null) {
//                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_top_item, null);
//                if(i == mViewPager.getCurrentItem()){
//                    view.setSelected(true);
//                }
//                tabAt.setCustomView(view);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;
        List<String> titles;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
        }
        public void addFragment(String title,Fragment fragment){
            titles.add(title);
            fragments.add(fragment);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    public static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        public void onTabSelected(TabLayout.Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition(),false);
        }

        public void onTabUnselected(TabLayout.Tab tab) {
        }

        public void onTabReselected(TabLayout.Tab tab) {
        }
    }

}
