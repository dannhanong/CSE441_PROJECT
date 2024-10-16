package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.FoodOptionRequest;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodOption;
import com.ktpm1.restaurant.repositories.FoodOptionRepository;
import com.ktpm1.restaurant.repositories.FoodRepository;
import com.ktpm1.restaurant.services.FoodOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodOptionServiceImpl implements FoodOptionService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodOptionRepository foodOptionRepository;

    @Override
    public FoodOption createFoodOption(FoodOptionRequest foodOptionRequest) {
        Food food = foodRepository.findById(foodOptionRequest.getFoodId()).orElse(null);
        return foodOptionRepository.save(FoodOption.builder()
                .name(foodOptionRequest.getName())
                .price(foodOptionRequest.getPrice())
                .food(food)
                .build());
    }

    @Override
    public FoodOption updateFoodOption(FoodOptionRequest foodOptionRequest, Long id) {
        FoodOption foodOption = foodOptionRepository.findById(id).orElse(null);
        foodOption.setName(foodOptionRequest.getName());
        foodOption.setPrice(foodOptionRequest.getPrice());
        return foodOptionRepository.save(foodOption);
    }

    @Override
    public void deleteFoodOption(Long id) {
        foodOptionRepository.deleteById(id);
    }

    @Override
    public FoodOption getFoodOptionById(Long id) {
        return foodOptionRepository.findById(id).orElse(null);
    }

    @Override
    public List<FoodOption> getFoodOptionsByFood(Long foodId) {
        Food food = foodRepository.findById(foodId).orElse(null);
        return foodOptionRepository.findByFood(food);
    }
}
