package com.finatext.investgate.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.finatext.investgate.MainApplication;
import com.finatext.investgate.data.SharePreferenceData;
import com.finatext.investgate.dj.AppComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by lenam on 6/10/16.
 */

public class BaseActivity extends AppCompatActivity {

    @Inject
    SharePreferenceData sharePreferenceData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getApplicationContext()).getAppComponent();
        appComponent.inject(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
