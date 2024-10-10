package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodHistory;
import com.ktpm1.restaurant.models.User;

import java.util.List;

public interface FoodHistoryService {
    void addFoodToHistory(Food food, User user);
    List<Food> getFoodHistory(String username);
}
