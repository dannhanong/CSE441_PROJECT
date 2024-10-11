package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.FoodOptionRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.FoodOption;
import com.ktpm1.restaurant.services.FoodOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-option")
public class FoodOptionController {
    @Autowired
    private FoodOptionService foodOptionService;

    @PostMapping("/create")
    public ResponseEntity<FoodOption> createFoodOption(@RequestBody FoodOptionRequest foodOptionRequest) {
        return ResponseEntity.ok(foodOptionService.createFoodOption(foodOptionRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FoodOption> updateFoodOption(@PathVariable Long id, @RequestBody FoodOptionRequest foodOptionRequest) {
        return ResponseEntity.ok(foodOptionService.updateFoodOption(foodOptionRequest, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFoodOption(@PathVariable Long id) {
        foodOptionService.deleteFoodOption(id);
        return ResponseEntity.ok(ResponseMessage.builder().message("Food option deleted successfully").build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FoodOption> getFoodOptionById(@PathVariable Long id) {
        return ResponseEntity.ok(foodOptionService.getFoodOptionById(id));
    }

    @GetMapping("/get-by-food/{foodId}")
    public ResponseEntity<List<FoodOption>> getFoodOptionsByFood(@PathVariable Long foodId) {
        return ResponseEntity.ok(foodOptionService.getFoodOptionsByFood(foodId));
    }
}
