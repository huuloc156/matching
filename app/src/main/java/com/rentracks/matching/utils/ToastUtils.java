package com.rentracks.matching.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by apple on 7/15/15.
 */
public final class ToastUtils {
    private static Toast currentToast;
    public static void show(Context context, String message){
        if(currentToast!=null){
            currentToast.cancel();
        }
        currentToast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        currentToast.show();
    }
}
