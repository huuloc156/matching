package com.finatext.investgate;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.finatext.investgate.dj.AppComponent;
import com.finatext.investgate.dj.AppModule;
import com.finatext.investgate.dj.DaggerAppComponent;

import io.fabric.sdk.android.Fabric;

/**
 * Created by lenam on 6/10/16.
 */

public class MainApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().build())
                .build();
        //TODO turn on crashlytic after change valid metadata in manifest
//        Fabric.with(this, crashlyticsKit);
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
