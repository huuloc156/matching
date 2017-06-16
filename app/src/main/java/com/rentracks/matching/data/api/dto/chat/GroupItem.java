package com.rentracks.matching.data.api.dto.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 6/12/17.
 */

public class GroupItem implements Parcelable {

    @SerializedName("group_name")
    public String group_name;


    @SerializedName("group_id")
    public int group_id;


    @SerializedName("last_name")
    public String last_name;

    @SerializedName("last_mess")
    public String last_mess;

    @SerializedName("pic")
    public String pic;

    public String getPic(){
        return pic;
    }


    @SerializedName("last_title")
    public String last_title;

    public GroupItem(){

    }

    protected GroupItem(Parcel in) {
        group_name = in.readString();
        group_id = in.readInt();
        last_name = in.readString();
        last_mess = in.readString();
        pic = in.readString();
        last_title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_name);
        dest.writeInt(group_id);
        dest.writeString(last_name);
        dest.writeString(last_mess);
        dest.writeString(pic);
        dest.writeString(last_title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupItem> CREATOR = new Creator<GroupItem>() {
        @Override
        public GroupItem createFromParcel(Parcel in) {
            return new GroupItem(in);
        }

        @Override
        public GroupItem[] newArray(int size) {
            return new GroupItem[size];
        }
    };
}
