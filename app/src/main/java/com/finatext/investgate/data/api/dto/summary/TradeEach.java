package com.finatext.investgate.data.api.dto.summary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by RENTRACKS VN3 on 7/15/2016.
 */

public class TradeEach  implements Serializable {
    @SerializedName("trade_type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("date")
    public String date;
    @SerializedName("trading_volumne")
    public String trading_volumne;
    @SerializedName("commission_fee")
    public String commission_fee;
    @SerializedName("interest")
    public String interest;

}
