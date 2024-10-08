package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.LoginForm;
import com.ktpm1.restaurant.dtos.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.LoginResponse;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginForm loginForm);
    @POST("auth/signup")
    Call<ResponseMessage> signup(@Body RegisterRequest registerRequest);
    @GET("/auth/get/profile")
    Call<User> getProfile(@Header("Authorization") String token);
}
