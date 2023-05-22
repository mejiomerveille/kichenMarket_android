package com.example.mervshops;

import com.example.mervshops.Models.LoginRequest;
import com.example.mervshops.Models.Post;
import com.example.mervshops.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

public interface ApiService {
   public static final String URL="http:// 192.168.124.79:8000/api";
   @POST("login")
   public Call<UserResponse> login(@Body LoginRequest loginRequest);
   @POST("user/register")
   public Call<String > userRegister(@Body User user);
   @GET("user/profile")
   public Call<String > userProfile();
   @GET("posts")
   public Call<List<Post>> postAll();
   @POST("posts/create")
   public Call<String> postCreate(@Body Post post);
   @POST("posts/update")
   public Call<List<Post> > postUpdate(@Body LoginRequest loginRequest);
   @POST("posts/delete")
   public Call<List<Post>> postDelete(@Body LoginRequest loginRequest);

}