package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.requests.CartRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CartApi {
    @POST("/cart/add")
    Call<ResponseMessage> addToCart(@Header("Authorization") String token,
                                    @Body CartRequest cartRequest);

    @GET("/cart/view")
    Call<Cart> viewCart(@Header("Authorization") String token);
}
