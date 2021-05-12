package com.example.familyhealthhandbook.API;

import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.Model.responseLogin;

import java.util.List;

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
}
