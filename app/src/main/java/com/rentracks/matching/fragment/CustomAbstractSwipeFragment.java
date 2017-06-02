package com.rentracks.matching.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rentracks.matching.R;
import com.rentracks.matching.fragment.header.IHeaderInfo;

import butterknife.BindView;

public abstract class CustomAbstractSwipeFragment extends BaseFragment{

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        return R.mipmap.header_filter;
    }

    @Override
    public int getHeaderLeftButtonImageResId() {
        return R.mipmap.header_setting;
    }

    @BindView(R.id.pager_swipe_custom)
    protected ViewPager mViewPager;
    @BindView(R.id.ll_fragment_custom_button)
    LinearLayout llButton;


    /*
    create 3 button : left title right

     */
    @BindView(R.id.btn_swip_left)
            Button btnLeft;
    @BindView(R.id.btn_swip_right)
            Button btnRight;
    @BindView(R.id.btn_swip_title)
    protected        Button btnTitle;

    protected AbstractSwipeFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    public CustomAbstractSwipeFragment(){

    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
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
        btnLeft.setOnClickListener(btnclick);
        btnRight.setOnClickListener(btnclick);
        btnTitle.setOnClickListener(btnclick);
        reAddButton();
    }

    protected abstract void initPagerFragment() ;

    public void addFragment(String title,Fragment fragment){
        mSectionsPagerAdapter.addFragment(title,fragment);
    }
    private void reAddButton(){
        int current_page = mViewPager.getCurrentItem();
        int all_page = mSectionsPagerAdapter.getCount();
        CharSequence title = mSectionsPagerAdapter.getPageTitle(current_page);
        btnTitle.setText(title);

        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);

        if(current_page == 0){
            btnLeft.setVisibility(View.INVISIBLE);
        }else if(current_page == all_page -1){
            btnRight.setVisibility(View.INVISIBLE);
        }


        /*create left button*/

//        for(int i = 0; i< mSectionsPagerAdapter.getCount(); i++){
//            CharSequence title = mSectionsPagerAdapter.getPageTitle(i);
//            LinearLayout llchild = new LinearLayout(getContext());
//            llchild.setLayoutParams( new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT,1f));
//            llchild.setGravity(Gravity.CENTER);
//
//            Button btn = new Button(getContext());
//            btn.setText(title);
//            btn.setLayoutParams(new LinearLayout.LayoutParams(
//                    200,
//                    200));
//            btn.setTag(i);
//            btn.setOnClickListener(btnclick);
//            setInitColor(btn, i%3, (i==0));
//            llchild.addView(btn);
//            llButton.addView(llchild);
//            llButton.setWeightSum(i+1);
//            listButton.add(btn);
//        }
    }
    Button.OnClickListener btnclick = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            String tagId = (String)v.getTag();
            int current_page = mViewPager.getCurrentItem();
            if(tagId.equals("left")){
                if(current_page - 1 > -1){
                    mViewPager.setCurrentItem(current_page-1);
                }

            }else if(tagId.equals("right")){
                if(current_page + 1 < mSectionsPagerAdapter.getCount() ){
                    mViewPager.setCurrentItem(current_page +1);
                }
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
//                for (int i =0;i< listButton.size(); i++){
//                    if(i == position){
//                        setInitColor(listButton.get(i),i%3, true);
//                    }else{
//                        setInitColor(listButton.get(i),i%3, false);
//                    }
//                }
                reAddButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        reAddButton();
//        mViewPager.setCurrentItem(0);
    }
    private int setInitColor(Button btn, int pos, boolean isCheck){
//        if(isCheck) {
//            switch (pos) {
//                case 0:
//                    btn.setBackgroundResource(R.drawable.tab_circle1);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
//                    break;
//                case 1:
//                    btn.setBackgroundResource(R.drawable.tab_circle2);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
//                    break;
//                case 2:
//                    btn.setBackgroundResource(R.drawable.tab_circle3);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.white));
//                    break;
//            }
//        }else{
//            switch (pos) {
//                case 0:
//                    btn.setBackgroundResource(R.drawable.tab_circle_check1);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.circle1));
//                    break;
//                case 1:
//                    btn.setBackgroundResource(R.drawable.tab_circle_check2);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.circle2));
//                    break;
//                case 2:
//                    btn.setBackgroundResource(R.drawable.tab_circle_check3);
//                    btn.setTextColor(getContext().getResources().getColor(R.color.circle3));
//                    break;
//            }
//        }
        return R.drawable.tab_circle1;
    }
}
