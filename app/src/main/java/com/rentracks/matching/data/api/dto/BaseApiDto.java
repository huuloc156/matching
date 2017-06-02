package com.rentracks.matching.data.api.dto;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.rentracks.matching.data.api.dto.search.PlaceMapItem;

import java.util.List;

/**
 * Created by apple on 10/8/15.
 */
public class BaseApiDto {
    public static final String STATUS_SUCCESS_201 = "201";
    public static final String STATUS_SUCCESS_200 = "200";
//    @SerializedName("current_time")
//    public Date currentTime;
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
//    @SerializedName("info")
//    public Object info;


    public boolean isSuccess() {
        if(TextUtils.isEmpty(status)){
            return false;
        }
//        String substring = status.substring(0, 3);
//        return STATUS_SUCCESS_201.equals(substring) || STATUS_SUCCESS_200.equals(substring);
        return status.equals("OK");
    }
}
