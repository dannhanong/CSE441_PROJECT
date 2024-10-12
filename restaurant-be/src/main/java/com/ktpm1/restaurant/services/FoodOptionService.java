package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.FoodOptionRequest;
import com.ktpm1.restaurant.models.FoodOption;

import java.util.List;

public interface FoodOptionService {
    FoodOption createFoodOption(FoodOptionRequest foodOptionRequest);
    FoodOption updateFoodOption(FoodOptionRequest foodOptionRequest, Long id);
    void deleteFoodOption(Long id);
    FoodOption getFoodOptionById(Long id);
    List<FoodOption> getFoodOptionsByFood(Long foodId);
}
