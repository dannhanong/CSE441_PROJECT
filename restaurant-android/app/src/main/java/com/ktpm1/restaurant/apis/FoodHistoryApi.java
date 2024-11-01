package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Food;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface FoodHistoryApi {
    @GET("/food-histories/my-food-history")
    Call<List<Food>> getMyFoodHistory(@Header("Authorization") String token,
                                      @Query("sort") String sort);
}
