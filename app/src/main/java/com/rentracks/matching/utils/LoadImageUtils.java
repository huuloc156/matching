package com.rentracks.matching.utils;

import android.content.Context;
import android.text.TextUtils;

import com.rentracks.matching.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by HuuLoc on 5/26/17.
 */
public final class LoadImageUtils {
    private LoadImageUtils() {

    }


    public static RequestCreator load(Context context, String avatar) {
        if(!TextUtils.isEmpty(avatar)){
            return Picasso.with(context).load(avatar);
        }else {
            return Picasso.with(context).load(R.mipmap.noimage);
        }
    }
}
