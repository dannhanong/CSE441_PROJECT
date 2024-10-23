package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Catalog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatalogApi {
    @GET("/catalogs/all")
    Call<List<Catalog>> getAllCatalogs(@Query("keyword") String keyword);
}
