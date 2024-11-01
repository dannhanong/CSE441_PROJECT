package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.responses.FoodDetailAndRelated;
import com.ktpm1.restaurant.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodApi {
    @GET("/foods/all")
    Call<List<Food>> getAllFoods(@Query("keyword") String keyword);

    @GET("/foods/all/by-session")
    Call<List<Food>> getAllFoodsBySession();

    @GET("/foods/{id}")
    Call<FoodDetailAndRelated> getFoodDetailAndRelated(@Path("id") Long id, @Header("Authorization") String token);

    @GET("/foods/get/{id}")
    Call<Food> getFoodById(@Path("id") Long id);
}
