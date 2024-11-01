package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TableApi {
    @GET("/table/by-catalog/{catalogId}")
    Call<List<Table>> getTablesByCatalog(@Path("catalogId") Long catalogId);
}
