package com.rentracks.matching.data.api.dto.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by apple on 6/20/16.
 */
public class EventSearchItem implements Parcelable{
    @SerializedName("eid")
    public int eid;

    @SerializedName("placeid")
    public int placeid;

    @SerializedName("title")
    public String title;

    @SerializedName("max_member")
    public int max_member;

    @SerializedName("description")
    public String description;

    @SerializedName("start_date")
    public String start_date;

    @SerializedName("start_time")
    public String start_time;

    @SerializedName("end_date")
    public String end_date;

    @SerializedName("end_time")
    public String end_time;

    @SerializedName("status")
    public int status;

    @SerializedName("place_name")
    public String place_name;

    @SerializedName("business_hours")
    public String business_hours;

    @SerializedName("address")
    public String address;

    @SerializedName("location")
    public String location;

    @SerializedName("latitude")
    public Double latitude;

    @SerializedName("longitude")
    public Double longitude;

    @SerializedName("distance")
    public Double distance;

    @SerializedName("pic")
    public String pic;

    @SerializedName("join_member")
    public List<UidItem> join_member;

    @SerializedName("is_join")
    public int is_join;

    @SerializedName("is_like")
    public int is_like;

    @SerializedName("is_host")
    public int is_host;


    protected EventSearchItem(Parcel in) {
        eid = in.readInt();
        placeid = in.readInt();
        title = in.readString();
        max_member = in.readInt();
        description = in.readString();
        start_date = in.readString();
        start_time = in.readString();
        end_date = in.readString();
        end_time = in.readString();
        status = in.readInt();
        place_name = in.readString();
        business_hours = in.readString();
        address = in.readString();
        location = in.readString();
        pic = in.readString();
        join_member = in.createTypedArrayList(UidItem.CREATOR);
        is_join = in.readInt();
        is_like = in.readInt();
        is_host = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eid);
        dest.writeInt(placeid);
        dest.writeString(title);
        dest.writeInt(max_member);
        dest.writeString(description);
        dest.writeString(start_date);
        dest.writeString(start_time);
        dest.writeString(end_date);
        dest.writeString(end_time);
        dest.writeInt(status);
        dest.writeString(place_name);
        dest.writeString(business_hours);
        dest.writeString(address);
        dest.writeString(location);
        dest.writeString(pic);
        dest.writeTypedList(join_member);
        dest.writeInt(is_join);
        dest.writeInt(is_like);
        dest.writeInt(is_host);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventSearchItem> CREATOR = new Creator<EventSearchItem>() {
        @Override
        public EventSearchItem createFromParcel(Parcel in) {
            return new EventSearchItem(in);
        }

        @Override
        public EventSearchItem[] newArray(int size) {
            return new EventSearchItem[size];
        }
    };

    public String getPic() {
//        if(avatarUrl == null){
//            if(BuildConfig.DEBUG){
//                //TODO test avatar
//                return "http://annathemagazineonline.com/wp-content/uploads/2015/09/Megan-Fox-from-Transformers-917-copy-780x488.jpg";
//            }
//            return null;
//        }
        if(pic == null){
            return null;
        }
//        pic = R.string.host_params + pic;
        return pic;
    }

}
