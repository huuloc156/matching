package com.rentracks.matching.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rentracks.matching.MainApplication;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.data.SharePreferenceData;
import com.rentracks.matching.data.api.MatchingApi;
import com.rentracks.matching.dj.AppComponent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by HuuLoc on 6/15/17.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected boolean isFirstCreate = true;

    @Inject
    protected SharePreferenceData preferenceData;
    @Inject
    protected MatchingApi matchingApi;
    @Inject
    Bus mBus;
    BaseActivity baseActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getActivity().getApplicationContext()).getAppComponent();
        appComponent.inject(this);
        mBus.register(this);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        baseActivity = (BaseActivity)context;
    }
    @Override
    public void onDestroy() {
        mBus.unregister(this);
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        isFirstCreate = false;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    protected <T> void androidSubcribe(Observable<T> observable, Subscriber<T> subscriber) {
        if (baseActivity != null) {
            baseActivity.androidSubcribe(observable, subscriber);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            //TODO to reload data, need restore instance.
            isFirstCreate = true;
        }


    }

}
