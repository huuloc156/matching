package com.finatext.investgate.data.api.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 10/14/15.
 */
public class ListDto<T> extends BaseApiDto{
    @SerializedName("data")
    public ListData<T> data;
}
