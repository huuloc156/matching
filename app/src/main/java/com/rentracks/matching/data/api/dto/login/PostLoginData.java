package com.rentracks.matching.data.api.dto.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 6/20/2016.
 */
public class PostLoginData {
    @SerializedName("email")
   public String email;
    @SerializedName("password")
    public String password;
}
