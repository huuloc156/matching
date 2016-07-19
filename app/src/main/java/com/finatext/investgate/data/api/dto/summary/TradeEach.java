package com.finatext.investgate.data.api.dto.summary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



public class TradeEach  implements Parcelable {
    @SerializedName("trade_type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("date")
    public String date;
    @SerializedName("trading_volumne")
    public int trading_volumne;
    @SerializedName("commission_fee")
    public int commission_fee;
    @SerializedName("interest")
    public int interest;
    public  TradeEach(){

    }
//    public TradeEach(String ty, String na, String da, int tr, int co, int in){
//        type = ty;
//        name = na;
//        date = da;
//        trading_volumne = tr;
//        commission_fee = co;
//        interest = in;
//    }
    protected TradeEach(Parcel in) {
        type = in.readString();
        name = in.readString();
        date = in.readString();
        trading_volumne = in.readInt();
        commission_fee = in.readInt();
        interest = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeInt(trading_volumne);
        dest.writeInt(commission_fee);
        dest.writeInt(interest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TradeEach> CREATOR = new Creator<TradeEach>() {
        @Override
        public TradeEach createFromParcel(Parcel in) {
            return new TradeEach(in);
        }

        @Override
        public TradeEach[] newArray(int size) {
            return new TradeEach[size];
        }
    };
}
