package com.finatext.investgate.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.finatext.investgate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public abstract class AbstrackSwipeFragmentCustom extends BaseFragment{

    @BindView(R.id.pager_swipe_custom)
    ViewPager mViewPager;
    @BindView(R.id.ll_fragment_custom_button)
    LinearLayout llButton;

    List<Button> listButton = new ArrayList<Button>();
    AbstractSwipeFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    public AbstrackSwipeFragmentCustom(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_swipe, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new AbstractSwipeFragment.SectionsPagerAdapter(getChildFragmentManager());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reAddButton();
    }

    protected abstract void initPagerFragment() ;

    public void addFragment(String title,Fragment fragment){
        mSectionsPagerAdapter.addFragment(title,fragment);
    }
    private void reAddButton(){
        llButton.removeAllViews();
        listButton.clear();
        for(int i = 0; i< mSectionsPagerAdapter.getCount(); i++){
            CharSequence title = mSectionsPagerAdapter.getPageTitle(i);
            LinearLayout llchild = new LinearLayout(getContext());
            llchild.setLayoutParams( new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            llchild.setGravity(Gravity.CENTER);

            Button btn = new Button(getContext());
            btn.setText(title);
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    200,
                    200));
            btn.setTag(i);
            btn.setOnClickListener(btnclick);
            setInitColor(btn, i%3, (i==0));
            llchild.addView(btn);
            llButton.addView(llchild);
            llButton.setWeightSum(i+1);
            listButton.add(btn);
        }
    }
    Button.OnClickListener btnclick = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            int tagId = (int)v.getTag();
            if(mViewPager.getCurrentItem() != tagId) {
                mViewPager.setCurrentItem(tagId);
            }
        }

    };

    protected void setupTabLayout() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i =0;i< listButton.size(); i++){
                    if(i == position){
                        setInitColor(listButton.get(i),i%3, true);
                    }else{
                        setInitColor(listButton.get(i),i%3, false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mViewPager.setCurrentItem(0);
    }
    private int setInitColor(Button btn, int pos, boolean isCheck){
        if(isCheck) {
            switch (pos) {
                case 0:
                    btn.setBackgroundResource(R.drawable.tab_circle1);
                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
                    break;
                case 1:
                    btn.setBackgroundResource(R.drawable.tab_circle2);
                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
                    break;
                case 2:
                    btn.setBackgroundResource(R.drawable.tab_circle3);
                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
                    break;
            }
        }else{
            switch (pos) {
                case 0:
                    btn.setBackgroundResource(R.drawable.tab_circle_check1);
                    btn.setTextColor(getContext().getResources().getColor(R.color.circle1));
                    break;
                case 1:
                    btn.setBackgroundResource(R.drawable.tab_circle_check2);
                    btn.setTextColor(getContext().getResources().getColor(R.color.circle2));
                    break;
                case 2:
                    btn.setBackgroundResource(R.drawable.tab_circle_check3);
                    btn.setTextColor(getContext().getResources().getColor(R.color.circle3));
                    break;
            }
        }
        return R.drawable.tab_circle1;
    }
}
