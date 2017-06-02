package com.rentracks.matching.dj;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.rentracks.matching.BuildConfig;
import com.rentracks.matching.R;
import com.rentracks.matching.data.SharePreferenceData;
import com.rentracks.matching.data.api.MatchingApi;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    OkHttpClient provideOkHttpClient(final Context context, final SharePreferenceData preferenceData) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
        }
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String versionName = "";
                try {
                    versionName = context.getPackageManager()
                            .getPackageInfo(context.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                //request fx header
//                newBuilder.addHeader("X-Matching-Client-OS", "android");
//                newBuilder.addHeader("X-Matching-Client-OS-Version", Build.VERSION.RELEASE);
//                newBuilder.addHeader("X-Matching-Client-Device", Build.MODEL);
//                newBuilder.addHeader("X-Matching-Client-Device-ID", android_id);
//                newBuilder.addHeader("X-Matching-Client-App-Version", versionName);
                //session
                newBuilder.addHeader("X-Matching-Token", preferenceData.getUserToken());
                return chain.proceed(newBuilder.build());
            }
        };
        builder.addInterceptor(interceptor);
        return builder.build();
    }
    @Singleton
    @Provides
    MatchingApi provideInvestgateApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mHost)
                .client(okHttpClient)
                .build();
        return retrofit.create(MatchingApi.class);
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
