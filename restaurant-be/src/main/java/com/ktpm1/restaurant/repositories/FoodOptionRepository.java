package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOptionRepository extends JpaRepository<FoodOption, Long> {
    List<FoodOption> findByFood(Food food);
}
