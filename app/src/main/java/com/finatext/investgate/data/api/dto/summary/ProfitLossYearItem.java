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
    @SerializedName("gate_t")
    public String type;
    public String companyname;
    @SerializedName("p_l")
    public String position_pl;
    @SerializedName("trade_date")
    public String datetime;
    public boolean isHeader;
    public String year;
}
