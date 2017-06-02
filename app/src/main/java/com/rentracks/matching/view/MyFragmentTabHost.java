package com.rentracks.matching.view;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

import timber.log.Timber;

/**
 * Created by apple on 3/13/16.
 */
public class MyFragmentTabHost extends FragmentTabHost {
    public MyFragmentTabHost(Context context) {
        super(context);
    }

    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        }catch (IllegalStateException ex){
            //ignore this exception
            Timber.w("ignore onAttachedToWindow IllegalStateException");
        }

    }

    @Override
    public void setCurrentTab(int index) {

        try {
            super.setCurrentTab(index);
        }catch (IllegalStateException ex){
            //ignore this exception
            Timber.w("ignore setCurrentTab IllegalStateException");
        }
    }
}
