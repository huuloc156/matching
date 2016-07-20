package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossYearItem {
    @SerializedName("status")
    public String status;
    @SerializedName("name")
    public String name;
    @SerializedName("gate_type")
    public String type;
    @SerializedName("gate_name")
    public String companyname;
    @SerializedName("p_l")
    public float position_pl;
    @SerializedName("trade_date")
    public String datetime;
    @SerializedName("stock_trade_id")
    public int stockTradeId;
    public boolean isHeader;
    public String year;
}
