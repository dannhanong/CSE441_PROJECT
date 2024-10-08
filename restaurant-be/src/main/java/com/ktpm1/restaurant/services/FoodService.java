package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.FoodRequest;
import com.ktpm1.restaurant.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {
    Food createFood(FoodRequest foodRequest);
    Food updateFood(FoodRequest foodRequest, Long id);
    void deleteFood(Long id);
    Food getFood(Long id);
    Page<Food> getAllFoods(Pageable pageable, String keyword);
    List<Food> getAllFoods(String keyword);
    List<Food> getAllFoodsBySessionTime();
}
