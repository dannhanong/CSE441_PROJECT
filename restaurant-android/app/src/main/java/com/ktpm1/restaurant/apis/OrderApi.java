package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.dtos.responses.VNPayMessage;
import com.ktpm1.restaurant.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("/orders/create-food-and-table")
    Call<VNPayMessage> createOrder(@Header("Authorization") String token);

    @POST("/orders/create-table-only")
    Call<List<Order>> createOrderTableOnly(@Header("Authorization") String token);
}
