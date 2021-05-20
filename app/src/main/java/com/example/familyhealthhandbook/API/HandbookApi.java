package com.example.familyhealthhandbook.API;

import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.Model.responseLogin;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HandbookApi {

    @FormUrlEncoded
    @POST("users/login")
    Call<responseLogin> SingIn(@Field("email") String username, @Field("password") String pass);

    @Multipart
    @POST("users/register")
    Call<responseLogin> Register(@Part("email") RequestBody email,
                                 @Part("password") RequestBody pass,
                                 @Part("name") RequestBody name,
                                 @Part("gender") int gender,
                                 @Part("yearOfBirth") int year,
                                 @Part MultipartBody.Part avatar);
}
