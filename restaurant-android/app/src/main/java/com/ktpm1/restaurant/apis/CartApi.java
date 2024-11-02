package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.requests.CartRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {
    @POST("/cart/add")
    Call<ResponseMessage> addToCart(@Header("Authorization") String token,
                                    @Body CartRequest cartRequest);

    @GET("/cart/view")
    Call<Cart> viewCart(@Header("Authorization") String token);

    @DELETE("/cart/remove/{cartItemId}")
    Call<ResponseMessage> removeFromCart(@Header("Authorization") String token,
                                         @Path("cartItemId") Long cartItemId);

    @DELETE("/cart/clear")
    Call<ResponseMessage> clearCart(@Header("Authorization") String token);

    @PUT("/cart/update/{cartItemId}/{quantity}")
    Call<ResponseMessage> updateCart(@Header("Authorization") String token,
                                    @Path("cartItemId") Long cartItemId,
                                    @Path("quantity") Integer quantity);
}
