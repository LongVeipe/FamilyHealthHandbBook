package com.example.familyhealthhandbook.API;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.Model.HealthRecord;
import com.example.familyhealthhandbook.Model.News;
import com.example.familyhealthhandbook.Model.MyNotification;
import com.example.familyhealthhandbook.Model.Post;
import com.example.familyhealthhandbook.Model.Sickness;
import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.Model.responseLogin;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @Multipart
    @POST("posts")
    Call<Void> CreatePost(@Header("access-token") String token,
                          @Part("idGroup") RequestBody idGroup,
                          @Part("idHealthRecord") RequestBody idHealthRecord);

    @Multipart
    @POST("users/join")
    Call<Group> JoinGroup(@Header("access-token") String token,
                               @Part("inviteCode") RequestBody inviteCode);

    @Multipart
    @POST("groups/")
    Call<Group> CreateGroup(@Header("access-token") String token,
                           @Part("name") RequestBody name,
                           @Part("description") RequestBody description,
                           @Part MultipartBody.Part avatar);

    @GET("news")
    Call<List<News>> GetNews();


    @GET("users/joined-groups")
    Call<List<Group>> GetJoinedGroups(@Header("access-token") String token);


    @GET("groups/{idGroup}/posts")
    Call<List<Post>> GetPostsByIdGroup(@Path("idGroup") String idGroup);

    @GET("users/{id}")
    Call<User> GetUserById(@Path("id") String idUser);

    @GET("users/health-records")
    Call<List<HealthRecord>> GetMyHealthRecords(@Header("access-token") String token);

    @Multipart
    @POST("health-records/")
    Call<HealthRecord> CreateNewHealthRecord(@Header("access-token") String token,
                                             @Part("location") RequestBody location,
                                             @Part("sickness") RequestBody sickness,
                                             @Part ArrayList<MultipartBody.Part> images);

    @GET("sicknesses")
    Call<ArrayList<Sickness>> getAllSickness();

    @DELETE("health-records/{id}")
    Call<Void> DeleteHealthRecord(@Path("id") String idHealthRecord, @Header("access-token") String token);

    @GET("groups/{idGroup}")
    Call<Group> GetGroupById(@Path("idGroup") String idGroup);

    @GET("groups/{idGroup}/members")
    Call<List<User>> GetMembers(@Path("idGroup") String idGroup);

    @Multipart
    @POST("groups/transfer-permission")
    Call<Void> TransferPermission(@Header("access-token") String token,
                                  @Part("idGroup") RequestBody idGroup,
                                  @Part("idUser") RequestBody idUser);

    @Multipart
    @POST("users/leave")
    Call<Void> OutGroup(@Header("access-token") String token, @Part("idGroup") RequestBody idGroup);

    @Multipart
    @POST("groups/kick")
    Call<Void> KickMember(@Header("access-token") String token,
                          @Part("idGroup") RequestBody idGroup,
                          @Part("idUser") RequestBody idUser);

    @GET("users/notifications")
    Call<List<MyNotification>> GetMyNotification(@Header("access-token") String token);

    @POST("users/help-me")
    Call<Void> CallSOS(@Header("access-token") String token);
}
