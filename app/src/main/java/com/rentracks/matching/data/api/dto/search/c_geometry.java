package com.rentracks.matching.data.api.dto.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 6/1/17.
 */

public class c_geometry implements Parcelable {

    public c_geometry(Double la, Double lg){
        location = new c_location(la, lg);
    }

    @SerializedName("location")
    public c_location location;

    protected c_geometry(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<c_geometry> CREATOR = new Creator<c_geometry>() {
        @Override
        public c_geometry createFromParcel(Parcel in) {
            return new c_geometry(in);
        }

        @Override
        public c_geometry[] newArray(int size) {
            return new c_geometry[size];
        }
    };
}
