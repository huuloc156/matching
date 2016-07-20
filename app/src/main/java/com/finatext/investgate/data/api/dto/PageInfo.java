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
    @SerializedName("sum_all")
    public int sum_all;
    @SerializedName("sum_stock")
    public int sum_stock;
    @SerializedName("sum_fx")
    public int sum_fx;

    @SerializedName("sum_pl_by_year")
    public int sum_pl_by_year;
    @SerializedName("sum_profit")
    public int sum_profit;
    @SerializedName("sum_loss")
    public int sum_loss;

}
