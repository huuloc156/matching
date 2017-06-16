package com.rentracks.matching.data.api.dto.chat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuuLoc on 6/12/17.
 */

public class ChatItem {


    @SerializedName("group_id")
    public int group_id;


    @SerializedName("message")
    public String message;

    @SerializedName("uid")
    public int uid;


    @SerializedName("status")
    public int status;


    @SerializedName("created_at")
    public String created_at;

    @SerializedName("pic")
    public String pic;

    public String getPic(){
        return pic;
    }
}
