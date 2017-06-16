package com.rentracks.matching.data.image;

import android.graphics.drawable.Drawable;

/**
 * Created by HuuLoc on 6/14/17.
 */

public class dataDrawable {
    Drawable drawable;
    int id;
    public dataDrawable(int i, Drawable d){
        id = i;
        drawable = d;
    }
    public int getId(){
        return id;
    }
    public Drawable getDrawable(){
        return drawable;
    }
}
