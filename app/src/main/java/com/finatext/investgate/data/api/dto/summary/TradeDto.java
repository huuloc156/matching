package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;



public class TradeDto<T>{
    @SerializedName("items_attr")
    public  T ValueData;
}
