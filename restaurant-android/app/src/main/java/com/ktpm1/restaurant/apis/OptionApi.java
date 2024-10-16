package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.FoodOption;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OptionApi {
    @GET("/food-option/get-by-food/{foodId}")
    Call<List<FoodOption>> getOptionsByFood(@Path("foodId") Long foodId);
}
