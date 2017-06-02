package com.rentracks.matching.data.api.dto.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 6/1/17.
 */

public class c_location implements Parcelable {

    public c_location(Double la, Double lg){
        lat = la;
        lng = lg;
    }

    @SerializedName("lat")
    public Double lat;
    @SerializedName("lng")
    public Double lng;

    protected c_location(Parcel in) {
    }

    public static final Creator<c_location> CREATOR = new Creator<c_location>() {
        @Override
        public c_location createFromParcel(Parcel in) {
            return new c_location(in);
        }

        @Override
        public c_location[] newArray(int size) {
            return new c_location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
