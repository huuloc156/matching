package com.finatext.investgate;

import android.app.Application;

import com.finatext.investgate.dj.AppComponent;
import com.finatext.investgate.dj.AppModule;
import com.finatext.investgate.dj.DaggerAppComponent;

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

    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
