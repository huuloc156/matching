package com.rentracks.matching.data.api.dto.search;


import com.google.gson.annotations.SerializedName;

public class ProfitLossYearDetailItem {
    @SerializedName("company_name")
    public String companyName;
    @SerializedName("p_l")
    public int p_l;
}
