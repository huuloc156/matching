package com.finatext.investgate.data.api.dto.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 6/20/2016.
 */
public class GetLoginData {
    @SerializedName("token")
    public String token;
    @SerializedName("user_id")
    public String user_id;
}
