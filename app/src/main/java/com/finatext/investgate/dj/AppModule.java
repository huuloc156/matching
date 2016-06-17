package com.finatext.investgate.dj;

import android.app.Application;
import android.content.Context;

import com.finatext.investgate.R;
import com.finatext.investgate.data.SharePreferenceData;
import com.finatext.investgate.data.api.InvestgateApi;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by lenam on 6/10/16.
 */
@Module
public class AppModule {

    private final Application application;
    private String mHost;

    public AppModule(Application application){
        this.application = application;
        mHost = application.getString(R.string.host);
    }

    @Singleton
    @Provides
    Context provideApplicationContext(){
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    InvestgateApi provideInvestgateApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mHost)
                .build();
        return retrofit.create(InvestgateApi.class);
    }

    @Singleton
    @Provides
    SharePreferenceData provideSharePreferenceData(Context context){
        return new SharePreferenceData(context);
    }

    @Singleton
    @Provides
    Bus provideBus(){
        return new Bus(ThreadEnforcer.MAIN);
    }


}
