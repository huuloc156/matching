package com.rentracks.matching.data.api.dto.search;

import com.google.gson.annotations.SerializedName;



public class TradeDto<T>{
    @SerializedName("items_attr")
    public  T valueData;
}
