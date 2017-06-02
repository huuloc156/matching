package com.rentracks.matching.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.rentracks.matching.R;

import java.util.List;

/**
 * Created by apple on 11/15/15.
 */
public final class CommonUtils {
    private CommonUtils() {

    }
    public static String getApiGoolgeSearch(){
        return "AIzaSyB3_n97RXI8M08WHwM88t19Nk0IwvL3KPc";
    }
    public static String getFullPicUrl(Context context, String pic){
        return context.getString(R.string.host) + context.getString(R.string.host_params) + pic;
    }
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static void openUrlInExternalBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            //TODO show message?
            return;
        }
        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        if (checkIntentAvaiable(context, intent)) {
            context.startActivity(intent);
        }
    }

    private static boolean checkIntentAvaiable(Context context, Intent intent2) {
        List<ResolveInfo> result = context.getPackageManager().queryIntentActivities(intent2, PackageManager.MATCH_DEFAULT_ONLY);
        if (result == null || result.isEmpty()) {
            return false;
        }
        return true;
    }
}
