package com.finatext.investgate.data.api.dto.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Toan on 17-06-2016.
 */
public class RegistrationItem {

    @SerializedName("token")
    public String token;
    @SerializedName("user_id")
    public String user_id;
}