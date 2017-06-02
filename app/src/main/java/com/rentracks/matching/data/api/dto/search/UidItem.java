package com.rentracks.matching.data.api.dto.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 5/27/17.
 */

public class UidItem implements Parcelable {
    @SerializedName("uid")
    public int uid;

    protected UidItem(Parcel in) {
        uid = in.readInt();
    }

    public static final Creator<UidItem> CREATOR = new Creator<UidItem>() {
        @Override
        public UidItem createFromParcel(Parcel in) {
            return new UidItem(in);
        }

        @Override
        public UidItem[] newArray(int size) {
            return new UidItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
    }
}
