package com.finatext.investgate.dj;

import com.finatext.investgate.activity.BaseActivity;
import com.finatext.investgate.fragment.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lenam on 6/10/16.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(BaseActivity activity);
    void inject(BaseFragment fragment);
}
