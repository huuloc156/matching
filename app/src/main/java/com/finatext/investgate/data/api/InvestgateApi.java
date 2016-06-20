package com.finatext.investgate.data.api;

import com.finatext.investgate.data.api.dto.login.RegistrationItem;
import com.finatext.investgate.data.api.dto.login.GetLoginData;
import com.finatext.investgate.data.api.dto.ObjectDto;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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

}
