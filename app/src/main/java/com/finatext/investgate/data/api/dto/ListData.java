package com.finatext.investgate.data.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by apple on 10/11/15.
 */
public class ListData<T> {
    @SerializedName("items_attr")
    public PageInfo pageInfo;
    @SerializedName("items")
    public List<T> items;
}
