package com.rentracks.matching.data.api.dto.search;

import com.google.gson.annotations.SerializedName;



public class TradeSummaryItem {
    @SerializedName("daily_profit_loss")
    public int daily_profit_loss;
    @SerializedName("daily_commission")
    public int daily_commission;
}
