package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.requests.BookingTableRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.models.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookingTableApi {
    @GET("/booking-table/status")
    Call<List<TableResponse>> getStatusTableByAvailable(@Query("start") String start,
                                                        @Query("additionalTime") int additionalTime,
                                                        @Query("catalogId") Long catalogId);

    @POST("/booking-table/create")
    Call<ResponseMessage> createBookingTable(@Header("Authorization") String token,
                                             @Body BookingTableRequest bookingTableRequest);
}
