package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.requests.ChangePasswordRequest;
import com.ktpm1.restaurant.dtos.requests.LoginForm;
import com.ktpm1.restaurant.dtos.requests.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.LoginResponse;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginForm loginForm);

    @POST("auth/signup")
    Call<User> signup(@Body RegisterRequest registerRequest);

    @GET("/auth/get/profile")
    Call<User> getProfile(@Header("Authorization") String token);

    @PUT("/auth/update-verify-code")
    Call<ResponseMessage> updateVerifyCode(@Query("username") String username,
                                           @Query("code") String code);

    @GET("/auth/verify")
    Call<ResponseMessage> verify(@Query("code") String code);

    @PUT("/auth/change-password")
    Call<ResponseMessage> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest request
    );

    @POST("/auth/logout")
    Call<ResponseMessage> logout(@Header("Authorization") String token   );
    @PUT("/auth/update/profile")
    @Multipart
    Call<ResponseMessage> updateProfile(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part avatar
    );

    @GET("/auth/resend-verify-code")
    Call<ResponseMessage> resendVerifyCode(@Query("phoneNumber") String phoneNumber);
}
