package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {
    @GET("/foods/all")
    Call<List<Food>> getAllFoods(
            @Query("keyword") String keyword
    );
}
