package com.rentracks.matching.data.api;

import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.chat.ChatItem;
import com.rentracks.matching.data.api.dto.chat.GroupItem;
import com.rentracks.matching.data.api.dto.login.GetLoginData;
import com.rentracks.matching.data.api.dto.login.RegistrationItem;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.data.api.dto.search.PlaceItem;
import com.rentracks.matching.data.api.dto.search.PlaceMapItem;
import com.rentracks.matching.data.api.dto.user.HobbyItem;
import com.rentracks.matching.data.api.dto.user.UserItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lenam on 6/10/16.
 */

public interface MatchingApi {

    @FormUrlEncoded
    @POST("/matching_app/login.php?action=forget_password")
    Observable<ObjectDto> forgetMail(@Field("email") String email);

    @FormUrlEncoded
    @POST("/matching_app/login.php?action=register")
    Observable<ObjectDto<RegistrationItem>> signUpMail(@Field("email") String email,
                                                       @Field("password") String password);
    @FormUrlEncoded
    @POST("/matching_app/login.php?action=login")
    Observable<ObjectDto<GetLoginData>> loginActivity(@Field("email") String email,
                                                      @Field("password") String password);


    @POST("/matching_app/login.php?action=login_fb_token")
    Observable<ObjectDto<GetLoginData>> loginFacebook();

    @POST("/matching_app/login.php?action=login_google_token")
    Observable<ObjectDto<GetLoginData>> loginGoogle();


    @FormUrlEncoded
    @POST("/matching_app/search.php?action=search_event")
    Observable<ListDtoData<EventSearchItem>> searchEvent(@Field("page") int page,
                                                         @Field("distance") int distance,
                                                         @Field("limit") int limit,
                                                         @Field("q") String search_text
    );

    @FormUrlEncoded
    @POST("/matching_app/event.php?action=list_past")
    Observable<ListDtoData<EventSearchItem>> pastEvent(@Field("page") int page,
                                                       @Field("limit") int limit
    );


    @FormUrlEncoded
    @POST("/matching_app/event.php?action=list_future")
    Observable<ListDtoData<EventSearchItem>> futureEvent(@Field("page") int page,
                                                       @Field("limit") int limit
    );


    @FormUrlEncoded
    @POST("/matching_app/event.php?action=list_owner")
    Observable<ListDtoData<EventSearchItem>> ownerEvent(@Field("page") int page,
                                                         @Field("limit") int limit
    );


    @FormUrlEncoded
    @POST("/matching_app/picture.php?action=get_event")
    Observable<ObjectDto<EventSearchItem>> getEventPic(@Field("eid") int eid );


    @FormUrlEncoded
    @POST("/matching_app/search.php?action=search_user")
     Observable<ListDtoData<UserItem>> searchUser(@Field("page") int page,
                                                        @Field("distance") int distance,
                                                        @Field("limit") int limit,
                                                        @Field("age_from") int age_from,
                                                        @Field("age_to") int age_to,
                                                        @Field("gender") int gender,
                                                        @Field("q") String search
    );


    @FormUrlEncoded
    @POST("/matching_app/event.php?action=join")
    Observable<ObjectDto<Integer>> joinEvent(@Field("eid") int eid);

    @FormUrlEncoded
    @POST("/matching_app/event.php?action=un_join")
    Observable<ObjectDto<Integer>> un_joinEvent(@Field("eid") int eid);


    @FormUrlEncoded
    @POST("/matching_app/event.php?action=like")
    Observable<ObjectDto<Integer>> likeEvent(@Field("eid") int eid);

    @FormUrlEncoded
    @POST("/matching_app/event.php?action=un_like")
    Observable<ObjectDto<Integer>> un_likeEvent(@Field("eid") int eid);

    @FormUrlEncoded
    @POST("/matching_app/user.php?action=detail")
    Observable<ObjectDto<UserItem>> userDetail(@Field("uid") int uid);


    @POST("/matching_app/suggest_hobby.php")
    Observable<ListDtoData<HobbyItem>> getHobby();

    @Multipart
    @POST("/matching_app/picture.php?action=save_user")
    Observable<ObjectDto> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);


    @Multipart
    @POST("/matching_app/picture.php?action=save_event")
    Observable<ObjectDto> uploadPictureEvent(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("eid") int eid);

    @FormUrlEncoded
    @POST("/matching_app/user.php?action=update")
    Observable<ObjectDto> EditUser(
                                            @Field("name") String name,
                                            @Field("gender") int gender,
                                            @Field("age") int age,
                                            @Field("location") String location,
                                            @Field("hobby") String hobby,
                                            @Field("description") String description
                                            );


    @FormUrlEncoded
    @POST("/matching_app/user.php?action=update")
    Observable<ObjectDto> updateLocation(@Field("latitude") Double latitude,
                                                      @Field("longitude") Double longitude);



    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
//    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Observable<PlaceMapItem<PlaceItem>> searchPlace(
                                        @Query("keyword") String query,
//                                        @Query("type") String type,
                                        @Query("location") String location,
                                        @Query("radius") int radius,
                                        @Query("key") String key
                                        );


    @FormUrlEncoded
    @POST("/matching_app/event.php?action=create")
    Observable<ObjectDto<EventSearchItem>> createEvent(@Field("title") String title,
                                                 @Field("max_member") int max_member,
                                                 @Field("description") String description,
                                                 @Field("start_date") String start_date,
                                                 @Field("start_time") String start_time,
                                                 @Field("place_name") String place_name,
                                                 @Field("address") String address,
                                                 @Field("latitude") Double latitude,
                                                 @Field("longitude") Double longitude
    );

    @FormUrlEncoded
    @POST("/matching_app/event.php?action=edit")
    Observable<ObjectDto<EventSearchItem>> editEvent(
                                                        @Field("eid") int eid,
                                                        @Field("title") String title,
                                                       @Field("max_member") int max_member,
                                                       @Field("description") String description,
                                                       @Field("start_date") String start_date,
                                                       @Field("start_time") String start_time,
                                                       @Field("place_name") String place_name,
                                                       @Field("address") String address,
                                                       @Field("latitude") Double latitude,
                                                       @Field("longitude") Double longitude
    );


    /*
    API friend status
    */
    @FormUrlEncoded
    @POST("/matching_app/user_relation.php?action=send_request")
    Observable<ObjectDto> sendRequest(@Field("uid") int uid);

    @FormUrlEncoded
    @POST("/matching_app/user_relation.php?action=cancel_request")
    Observable<ObjectDto> cancelRequest(@Field("friendid") int uid);

    @FormUrlEncoded
    @POST("/matching_app/user_relation.php?action=accept_request")
    Observable<ObjectDto> acceptRequest(@Field("friendid") int uid);


    @FormUrlEncoded
    @POST("/matching_app/user_device.php?action=update")
    Observable<ObjectDto> updateDeviceToken(@Field("device_token") String device_token);



    @FormUrlEncoded
    @POST("/matching_app/message.php?action=get_list_group")
    Observable<ListDtoData<GroupItem>> getListGroup(@Field("page") int page,
                                                    @Field("q") String search,
                                                    @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST("/matching_app/message.php?action=create_group")
    Observable<ObjectDto<GroupItem>> createGroup(@Field("group_name") String group_name,
                                                    @Field("uids") String user_ids
    );


    @FormUrlEncoded
    @POST("/matching_app/message.php?action=get_group_detail")
    Observable<ListDtoData<ChatItem>> getListChat(@Field("group_id") int group_id,
                                                  @Field("date_time") String date_time,
                                                  @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST("/matching_app/message.php?action=get_user_chat_detail")
    Observable<ListDtoData<ChatItem>> getList2UserChat(@Field("uid") int uid,
                                                  @Field("date_time") String date_time,
                                                  @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST("/matching_app/message.php?action=send_group")
    Observable<ObjectDto> sendMessToGroup(@Field("group_id") int group_id,
                                                  @Field("mess") String mess,
                                                  @Field("title") String title
    );
    @FormUrlEncoded
    @POST("/matching_app/message.php?action=send_mess")
    Observable<ObjectDto> sendMessToUser(@Field("uid") int user_id,
                                          @Field("mess") String mess,
                                          @Field("title") String title
    );
}
