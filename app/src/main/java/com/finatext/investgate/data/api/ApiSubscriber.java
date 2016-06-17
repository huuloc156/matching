package com.finatext.investgate.data.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.crashlytics.android.Crashlytics;
import com.finatext.investgate.BuildConfig;
import com.finatext.investgate.R;
import com.finatext.investgate.data.api.dto.BaseApiDto;
import com.finatext.investgate.utils.ToastUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by apple on 10/11/15.
 */
public abstract class ApiSubscriber<T extends BaseApiDto> extends Subscriber<T> {
    private final Activity context;
    boolean isShowErrorDialog = true;

    public ApiSubscriber(Activity activity){
        this.context = activity;

    }
    public ApiSubscriber(Activity activity, boolean isShowErrorDialog){
        this.context = activity;
        this.isShowErrorDialog = isShowErrorDialog;
    }
    public void setIsShowErrorDialog(boolean isShowErrorDialog) {
        this.isShowErrorDialog = isShowErrorDialog;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if(BuildConfig.DEBUG){
            e.printStackTrace();
        }
        if(e instanceof HttpException) {
            //TODO handle retrofit 2 error
            if(isShowErrorDialog) {
                showUpdateFailedDialog(context);
            }
        }else {
            //log to Crashlytics
            Crashlytics.logException(e);
            if(isShowErrorDialog) {
                showUpdateFailedDialog(context,"エラーが発生しました。\nしばらくしてから再度お試して下さい。");
            }
        }

        onCompleted();
    }

    @Override
    public final void onNext(T t) {
        if(t.isSuccess()){
            onDataSuccess(t);
        }else{
            //TODO handle error dialog message

            //TODO handle session expire
            if("401-000".equals(t.status)){
                ToastUtils.show(context, t.message);
            }else{
                if(isShowErrorDialog) {
                    if (BuildConfig.DEBUG) {
                        showUpdateFailedDialog(context, t.status + t.message);
                    } else {
                        showUpdateFailedDialog(context, t.message);
                    }
                }
            }
            onDataError(t);
        }
        onCompleted();
    }

    protected abstract void onDataError(T t);

    public abstract void onDataSuccess(T t);

    public static void showUpdateFailedDialog(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setMessage(R.string.dialog_network_error_msg)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        dialog.show();
    }
    public static void showUpdateFailedDialog(Activity activity, String msg) {
        if (activity == null || activity.isFinishing()) return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        dialog.show();
    }
}
