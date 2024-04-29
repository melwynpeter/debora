package com.mel.debora_v11.utilities;

import com.mel.debora_v11.api.ApiResponseModel;
import com.mel.debora_v11.api.YoutubeDataModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiSet {
    @FormUrlEncoded
    @POST("file path")
    Call<ApiResponseModel> getRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("/youtube/v3/search")
    Call<YoutubeDataModel> getYoutubeSearch(
            @Query("key") String developerKey,
            @Query("order") String order,
            @Query("q") String q,
            @Query("part") String part,
            @Query("type") String type
    );



}
