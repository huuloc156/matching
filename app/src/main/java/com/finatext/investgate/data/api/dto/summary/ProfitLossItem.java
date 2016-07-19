package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossItem {
//   public String status;
     @SerializedName("gate_type")
    public String type;
    public String name;

//    public String companyname;
    public String datetime;
    @SerializedName("p_l")
    public float position_pl;
    @SerializedName("year")
    public int year;
    public boolean isHeader;
}
