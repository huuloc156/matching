package com.rentracks.matching.data.api.dto.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 5/28/17.
 */

public class UserItem implements Parcelable {
    @SerializedName("name")
    public String name;
    @SerializedName("location")
    public String location;
    @SerializedName("mail")
    public String email;
    @SerializedName("age")
    public int age;
    @SerializedName("gender")
    public int gender;
    @SerializedName("description")
    public String description;
    @SerializedName("hobby")
    public String hobby;
    @SerializedName("pic")
    public String pic;

    public boolean isOwner = true;
    public int uid;

    @SerializedName("is_friend")
    public int is_friend;
    @SerializedName("is_block")
    public int is_block;


    protected UserItem(Parcel in) {
        name = in.readString();
        location = in.readString();
        email = in.readString();
        age = in.readInt();
        gender = in.readInt();
        description = in.readString();
        hobby = in.readString();
        pic = in.readString();
        isOwner = in.readByte() != 0;
        uid = in.readInt();
        is_friend = in.readInt();
        is_block = in.readInt();
    }

    public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };

    public String getPic() {
        if(pic == null){
            return null;
        }
        return pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeInt(age);
        dest.writeInt(gender);
        dest.writeString(description);
        dest.writeString(hobby);
        dest.writeString(pic);
        dest.writeByte((byte) (isOwner ? 1 : 0));
        dest.writeInt(uid);
        dest.writeInt(is_friend);
        dest.writeInt(is_block);
    }
}
