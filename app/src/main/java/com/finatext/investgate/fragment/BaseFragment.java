package com.finatext.investgate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.finatext.investgate.MainApplication;
import com.finatext.investgate.dj.AppComponent;

/**
 * Created by lenam on 6/10/16.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getActivity().getApplicationContext()).getAppComponent();
        appComponent.inject(this);
    }
}
