package com.rentracks.matching.data.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.rentracks.matching.BuildConfig;
import com.rentracks.matching.R;
import com.rentracks.matching.data.api.dto.BaseApiDto;
import com.rentracks.matching.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
                if(((HttpException) e).code() == 302) {
                    try {
                        JSONObject jObjError = new JSONObject(((HttpException) e).response().errorBody().string());
                        String message = jObjError.getString("message");
                        showUpdateFailedDialog(context, message);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }else {
            //log to Crashlytics TODO
//            Crashlytics.logException(e);
            if(isShowErrorDialog) {
                showUpdateFailedDialog(context," something error :(((");
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