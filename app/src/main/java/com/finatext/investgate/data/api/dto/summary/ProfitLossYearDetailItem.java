package com.finatext.investgate.data.api.dto.summary;


import com.google.gson.annotations.SerializedName;

public class ProfitLossYearDetailItem {
    @SerializedName("company_name")
    public String companyName;
    @SerializedName("p_l")
    public int p_l;
}
