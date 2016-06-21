package com.finatext.investgate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.SeekBar;

import com.finatext.investgate.MainApplication;
import com.finatext.investgate.R;
import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.activity.MainActivity;
import com.finatext.investgate.data.SharePreferenceData;
import com.finatext.investgate.data.api.InvestgateApi;
import com.finatext.investgate.dj.AppComponent;
import com.finatext.investgate.fragment.header.IHeaderInfo;
import com.finatext.investgate.fragment.header.IHeaderStateChange;
import com.finatext.investgate.fragment.header.OnBackPressListener;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by lenam on 6/10/16.
 */

public abstract class BaseFragment extends Fragment implements OnBackPressListener,IHeaderInfo {

    @Inject
    SharePreferenceData preferenceData;
    @Inject
    InvestgateApi investgateApi;
    protected int mContainerId = R.id.frame_tab_content2;
    @Inject
    Bus mBus;
    protected boolean isFirstCreate = true;
    protected String mCustomHeaderText;
    boolean isPause;
    BaseActivity baseActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getActivity().getApplicationContext()).getAppComponent();
        appComponent.inject(this);
        mBus.register(this);
    }

    @Override
    public void onDestroy() {
        mBus.unregister(this);
        super.onDestroy();
    }

    protected <T> void androidSubcribe(Observable<T> observable, Subscriber<T> subscriber) {
        if (baseActivity != null) {
            baseActivity.androidSubcribe(observable, subscriber);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.tag("kitvs").i("-----onActivityCreated " + this.getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.tag("kitvs").i("<<--onDetach " + this.getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.tag("kitvs").i("<<++++onSaveInstanceState " + this.getClass().getSimpleName());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Timber.tag("kitvs").i(">>++++onViewStateRestored " + this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        Timber.tag("kitvs").i(">>++++onResume " + this.getClass().getSimpleName());
//        mBus.register(this);
        checkHeader();
    }

    protected void checkHeader() {
        FragmentActivity activity = getActivity();
        if (isVisible() && activity instanceof IHeaderStateChange) {
            //TODO check header
            ((IHeaderStateChange) activity).checkHeaderState();
            if (getParentFragment() instanceof TabFragment) {
                ((IHeaderStateChange) activity).setHeaderInfo(this, mCustomHeaderText);
            }
        }
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @Override
    public int getHeaderTitleResId() {
        return R.string.app_name;
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        return 0;
    }

    @Override
    public int getHeaderLeftButtonImageResId() {
        return 0;
    }

    @Override
    public void onClickHeaderLeftButton(View view) {

    }

    @Override
    public void onClickHeaderRightButton(View view) {

    }

    @Override
    public boolean haveHeaderBackButton() {
        return true;
    }

    @Override
    public int getHeaderSearchHintResId() {
        return R.string.search_hint_default;
    }

    @Override
    public String getSearchKeyword() {
        return null;
    }

    public void goBack() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onPause() {
        isPause = true;
        Timber.tag("kitvs").i("----onPause " + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            //TODO to reload data, need restore instance.
            isFirstCreate = true;
        }
        Timber.tag("kitvs").i("----onViewCreated " + this.getClass().getSimpleName() + " save bundle " + savedInstanceState);
        if (getRootFragment() instanceof TabFragment && !(this instanceof TabFragment)) {
            if (((TabFragment) getRootFragment()).isChangeTab) {
                view.clearAnimation();
                ((TabFragment) getRootFragment()).isChangeTab = false;
            }
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                checkHeader();
            }
        });

    }

    @Override
    public void onDestroyView() {
        isFirstCreate = false;
        super.onDestroyView();
    }

    protected BaseFragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment != null && fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        if (fragment == null) {
            fragment = this;
        }
        return (BaseFragment) fragment;
    }

    public void showProgressDialog() {
        baseActivity.showProgressDialog();
    }

    public void closeDialog() {
        baseActivity.closeDialog();
    }

    public void startFragment(Fragment fragment, boolean isAddToBackStack) {
        startFragment(fragment, mContainerId, isAddToBackStack);
    }

    public void startFragment(Fragment fragment, int containerId, boolean isAddToBackStack) {
        if (isPause) {
            //prevent IllegalStateException of replace fragment
            Timber.w("startFragment prevent IllegalStateException of replace fragment");
            return;
        }
        if (fragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("containerId", mContainerId);
            fragment.setArguments(bundle);
        }
        FragmentTransaction ft = getRootFragment().getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit);
        ft.replace(containerId, fragment);

//        ft.setCustomAnimations(R.anim.widget_push_left_in,R.anim.widget_push_right_in,R.anim.widget_push_left_out,R.anim.widget_push_right_out);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        //call start activity from root because nest fragment not receive onActivityResult
        if (this instanceof TabFragment) {
            super.startActivityForResult(intent, requestCode);
        } else {
            getRootFragment().startActivityForResult(intent, requestCode);
        }
    }

    public void backToRoot() {
        if (isPause) {
            //prevent IllegalStateException of replace fragment
            Timber.w("backToRoot prevent IllegalStateException of replace fragment");
            return;
        }
        FragmentManager supportFragmentManager = getRootFragment().getChildFragmentManager();
        int stackEntryCount = supportFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackEntryCount; i++) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
//        while (stackEntryCount >0) {
//            supportFragmentManager.popBackStackImmediate();
//        }
    }

    @Override
    public boolean onBackPress() {
        return false;
    }


}
