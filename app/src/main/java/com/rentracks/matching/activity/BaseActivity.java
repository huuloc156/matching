package com.rentracks.matching.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rentracks.matching.MainApplication;
import com.rentracks.matching.R;
import com.rentracks.matching.data.SharePreferenceData;
import com.rentracks.matching.data.api.MatchingApi;
import com.rentracks.matching.dj.AppComponent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lenam on 6/10/16.
 */

public class BaseActivity extends AppCompatActivity {

    private SparseArray<Subscription> mSubscriptions = new SparseArray<>();
    private ProgressDialog dialog;

    @Inject
    protected SharePreferenceData sharePreferenceData;
    @Inject
    protected MatchingApi matchingApi;
    @Inject
    Bus mBus;

    protected boolean isPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getApplicationContext()).getAppComponent();
        appComponent.inject(this);
        initActionbar();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    private void initActionbar() {
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null){
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setElevation(0);
        }
    }

    @Override
    protected void onPause() {
        try {
            mBus.unregister(this);
        }catch (IllegalArgumentException ex){
            //ignore this exception
            Timber.tag("bus").e(ex.getMessage());
        }
        isPause = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        try{
            mBus.register(this);
        }
        catch (Exception e){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mBus.register(BaseActivity.this);
                    }catch (IllegalArgumentException ex){
                        //ignore this exception java.lang.IllegalArgumentException: Object already registered.
                        Timber.tag("bus-registry").e(ex.getMessage());
                    }

                }
            },500);
            Timber.e("Resume mBus crashed ->> Retry after 500ms");
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
    }
    public void showKeyboard(EditText edtHeaderSearchBox) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtHeaderSearchBox, InputMethodManager.SHOW_IMPLICIT);
    }
    public void hideKeyBoard(EditText et){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }
    ///////TASK
    public  <T> Subscription androidSubcribe(Observable<T> observable, Subscriber<T> subscriber){
        final int position = mSubscriptions.size();
        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        mSubscriptions.remove(position);
                    }
                })
                .subscribe(subscriber);
        mSubscriptions.append(position, subscription);
        return subscription;
    }

    public  <T> Subscription androidSubcribe(Observable<T> observable, Subscriber<T> subscriber, Scheduler scheduler){
        final int position = mSubscriptions.size();
        Subscription subscription = observable.subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        mSubscriptions.remove(position);
                    }
                })
                .subscribe(subscriber);
        mSubscriptions.append(position, subscription);
        return subscription;
    }

    public void showProgressDialog() {
        if(isFinishing()){
            return;
        }
        if (dialog == null) {
            dialog = ProgressDialog.show(this, null, "Loading...");
            if (dialog != null){
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
            }
        } else {
            dialog.show();
        }
    }

    public void closeDialog() {
        if(isFinishing()){
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        closeDialog();
        dialog = null;
        for (int i = 0; i< mSubscriptions.size(); i++){
            int key  = mSubscriptions.keyAt(i);
            Subscription subs = mSubscriptions.get(key);
            if (subs !=null) subs.unsubscribe();
        }
        super.onDestroy();
    }
    protected void showMess(String mess){
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
    }
    public void showpopupStatus(final boolean status, String content, final View.OnClickListener onClickPositiveButton) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_status);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_bg);
        dialog.setCancelable(false);
        TextView txtSuccess = (TextView) dialog.findViewById(R.id.txt_popup_status_success);
        TextView txtError = (TextView) dialog.findViewById(R.id.txt_popup_status_error);
        Button btnOK = (Button) dialog.findViewById(R.id.btn_popup_status_ok);
        if (status == true) {
            if (content != null) {
                txtSuccess.setText(content);
            }
            txtError.setVisibility(View.GONE);
        } else {
            if (content != null) {
                txtError.setText(content);
            }
            txtSuccess.setVisibility(View.GONE);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickPositiveButton != null) {
                    onClickPositiveButton.onClick(v);
                }
            }
        });
        dialog.show();
    }
}
