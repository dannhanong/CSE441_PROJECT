package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookingTableApi {
    @GET("/booking-table/status")
    Call<List<Table>> getStatusTableByAvailable(@Query("start") String start,
                                                @Query("additionalTime") int additionalTime,
                                                @Query("catalogId") Long catalogId);
}
