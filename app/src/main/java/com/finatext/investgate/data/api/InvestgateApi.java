package com.finatext.investgate.data.api;

import com.finatext.investgate.data.api.dto.ListDto;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.login.GetLoginData;
import com.finatext.investgate.data.api.dto.login.RegistrationItem;
import com.finatext.investgate.data.api.dto.summary.ProfitLossItem;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearDetailItem;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearItem;
import com.finatext.investgate.data.api.dto.summary.TradeEach;
import com.finatext.investgate.data.api.dto.summary.TradeHistoryItem;
import com.finatext.investgate.data.api.dto.summary.TradeSummaryItem;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lenam on 6/10/16.
 */

public interface InvestgateApi {

    @FormUrlEncoded
    @POST("/api/v1/users.json")
    Observable<ObjectDto<RegistrationItem>> signUpMail(@Field("email") String email,
                                                       @Field("password") String password,
                                                       @Field("password_confirmation") String password_confirmation);
    @FormUrlEncoded
    @POST("/api/v1/login.json")
    Observable<ObjectDto<GetLoginData>> loginActivity(@Field("email") String email,
                                                   @Field("password") String password);


    @GET("/api/v1/daily_trade_summary.json")
    Observable<ObjectDto<TradeSummaryItem>> getTradeSummary();


    @GET("/api/v1/yearly_pl.json")
    Observable<ListDto<ProfitLossItem>> getTradeSummaryYear();

    @GET("/api/v1/daily_pl_summary.json")
    Observable<ListDto<ProfitLossYearItem>> getDailyPLSummary(@Query("page") int page);

    @GET("/api/v1/daily_trade_list.json")
    Observable<ListDto<TradeHistoryItem>> getDailyTradeList(@Query("page") int page, @Query("gate_type") String type);

    @GET("/api/v1/each_trade.json")
    Observable<ObjectDto<TradeEach>> getEachTrade(@Query("stock_trade_id") int page);

    @GET("/api/v1/yearly_pl_summary.json")
    Observable<ListDto<ProfitLossYearDetailItem>> getYearlySummary(@Query("year") int year);
}
