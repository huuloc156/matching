package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;



public class TradeSummaryItem {
    @SerializedName("daily_profit_loss")
    public String daily_profit_loss;
    @SerializedName("daily_commission")
    public String daily_commission;
}