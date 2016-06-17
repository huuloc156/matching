package com.finatext.investgate.data.api.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 10/11/15.
 */
public class PageInfo {
    @SerializedName("page")
    public int page;
    @SerializedName("last_page")
    public int lastPage;
    @SerializedName("total_items")
    public int totalItems;
}
