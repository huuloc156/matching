package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;


public class TradeHistoryItem {
    @SerializedName("date")
    public String datetime;
    @SerializedName("trade_volumne")
    public float TradeVol;
    @SerializedName("name")
    public String name;
    @SerializedName("status")
    public String status;
    @SerializedName("asset_type")
    public String type;
    @SerializedName("company_name")
    public String company_name;
    @SerializedName("stock_trade_id")
    public int stockTradeId;
    public boolean isHeader;
    public String year;
}
