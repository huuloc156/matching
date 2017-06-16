package com.rentracks.matching.dj;

import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.fragment.BaseDialogFragment;
import com.rentracks.matching.fragment.BaseFragment;

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
    void inject(BaseDialogFragment fragment);
}
