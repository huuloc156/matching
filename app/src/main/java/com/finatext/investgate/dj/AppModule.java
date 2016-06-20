package com.finatext.investgate.dj;

import android.app.Application;
import android.content.Context;

import com.finatext.investgate.BuildConfig;
import com.finatext.investgate.R;
import com.finatext.investgate.data.SharePreferenceData;
import com.finatext.investgate.data.api.InvestgateApi;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lenam on 6/10/16.
 */
@Module
public class AppModule {

    private final Application application;
    private String mHost;

    public AppModule(Application application) {
        this.application = application;
        mHost = application.getString(R.string.host);
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
        }
        return builder.build();
    }
    @Singleton
    @Provides
    InvestgateApi provideInvestgateApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mHost)
                .client(okHttpClient)
                .build();
        return retrofit.create(InvestgateApi.class);
    }

    @Singleton
    @Provides
    SharePreferenceData provideSharePreferenceData(Context context) {
        return new SharePreferenceData(context);
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus(ThreadEnforcer.MAIN);
    }


}
