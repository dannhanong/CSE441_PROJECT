package com.ktpm1.restaurant;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.SessionTime;
import com.ktpm1.restaurant.repositories.FoodRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RestaurantBeApplicationTests {
	@Autowired
	private FoodRepository foodRepository;

	@Test
	void contextLoads() {
		List<Food> foods = foodRepository.findAllBySessionTime(SessionTime.MORNING);
		System.out.println(foods.size());
	}

}
