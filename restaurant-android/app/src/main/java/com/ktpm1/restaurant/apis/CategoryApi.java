package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("/categories/all")
    Call<List<Category>> getAllCategories();
}
