package com.rentracks.matching;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.rentracks.matching.dj.AppComponent;
import com.rentracks.matching.dj.AppModule;
import com.rentracks.matching.dj.DaggerAppComponent;

import timber.log.Timber;

/**
 * Created by lenam on 6/10/16.
 */

public class MainApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        else{
//            Timber.plant(null);
        }
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
