package com.rentracks.matching.data.api.dto.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.rentracks.matching.utils.CommonUtils;

import java.util.List;

/**
 * Created by HuuLoc on 5/30/17.
 */

public class PlaceItem implements Parcelable{
    public PlaceItem(){

    }
    @SerializedName("formatted_address")
    public String formatted_address;
    @SerializedName("name")
    public String name;


    @SerializedName("geometry")
    public c_geometry geometry;

    protected PlaceItem(Parcel in) {
        formatted_address = in.readString();
        name = in.readString();
    }

    public static final Creator<PlaceItem> CREATOR = new Creator<PlaceItem>() {
        @Override
        public PlaceItem createFromParcel(Parcel in) {
            return new PlaceItem(in);
        }

        @Override
        public PlaceItem[] newArray(int size) {
            return new PlaceItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formatted_address);
        dest.writeString(name);
    }



    @SerializedName("photos")
    public List<c_photos> photos;

    public class c_photos {
        @SerializedName("photo_reference")
        public String photo_reference;
    }


    public String getPic(int maxWidth){
        String photo_reference = "";
        if(this.photos != null && this.photos.size() > 0){
            photo_reference = this.photos.get(0).photo_reference;
        }
        if(photo_reference.equals("")){
            return null;
        }
        String pic = "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+maxWidth+"&photoreference="+ photo_reference+"&key="+ CommonUtils.getApiGoolgeSearch();
        return pic;
    }
}
