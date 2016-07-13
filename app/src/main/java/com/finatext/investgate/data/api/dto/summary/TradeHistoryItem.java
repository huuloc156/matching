package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RENTRACKS VN3 on 7/12/2016.
 */

public class TradeHistoryItem {
    @SerializedName("date")
    public String datetime;
    @SerializedName("trade_volumne")
    public String TradeVol;
    @SerializedName("name")
    public String name;
    @SerializedName("status")
    public String status;
    @SerializedName("asset_type")
    public String type;
    @SerializedName("company_name")
    public String company_name;
    public boolean isHeader;
    public String year;
}
