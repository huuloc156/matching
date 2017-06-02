package com.rentracks.matching.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rentracks.matching.R;
import com.rentracks.matching.activity.login.LoginActivity;

import timber.log.Timber;


public class SplashActivity extends BaseActivity implements Animation.AnimationListener {

    private String token;
    private String userId;
    private View splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        token = sharePreferenceData.getUserToken();
        userId = sharePreferenceData.getUserId();

        setContentView(R.layout.activity_splash);
        splashView = findViewById(R.id.img_splash);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= 11) {
            window.getDecorView().setSystemUiVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        splashView.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInitialzie();
            }
        }, 1000);
        startAmination();
    }

    private void checkInitialzie() {
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Timber.tag("gcm-push-handle").i("onNewIntent type" + extras.getString("type"));
            checkStartActivity();
        }
    }


    private void startAmination() {
        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animationFadeIn.setAnimationListener(this);
        splashView.startAnimation(animationFadeIn);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //TODO check token valid?
        checkStartActivity();
    }

    protected void checkStartActivity() {
        if (
                TextUtils.isEmpty(token)
//                || TextUtils.isEmpty(userId)
                ) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        } else {
            Intent intent = new Intent(this, MainActivity.class);
//            Intent thisIntent = getIntent();
//            if (thisIntent != null && CLICK_ACTION_DEFAULT.equals(thisIntent.getAction())) {
//                Bundle extras = thisIntent.getExtras();
//                if (extras != null) {
//                    Timber.tag(TAG_GCM_PUSH_HANDLE).i("start main activity with extra " + extras + " " + extras.keySet());
//
//                    Timber.tag(TAG_GCM_PUSH_HANDLE).i("istaskroot " + isTaskRoot());
//                    if (isTaskRoot()) {
//                        //Use FLAG_ACTIVITY_MULTIPLE_TASK because of in lolliop device
//                        //start intent of CLICK_ACTION_DEFAULT from gcm lib will not run splash activity again, it bring the app task to front
//                        //so wrong the notification flows.
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    } else {
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    }
//                    intent.putExtra(EXTRA_NOTIFICATION_DATA, extras);
//                }
//            } else {
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
