package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodHistory;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.FoodHistoryRepository;
import com.ktpm1.restaurant.repositories.FoodRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.FoodHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodHistoryServiceImpl implements FoodHistoryService {
    @Autowired
    private FoodHistoryRepository foodHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public void addFoodToHistory(Food food, User user) {
        FoodHistory foodHistory = new FoodHistory();
        foodHistory.setFood(food);
        foodHistory.setUser(user);
        foodHistoryRepository.save(foodHistory);
    }

    @Override
    public List<Food> getFoodHistory(String username) {
        User user = userRepository.findByUsername(username);
        List<FoodHistory> foodHistories = foodHistoryRepository.findByUser(user);
        return foodHistories.stream().map(FoodHistory::getFood).toList();
    }
}
