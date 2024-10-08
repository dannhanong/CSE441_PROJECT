package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.FoodRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foods")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("/all")
    public ResponseEntity<List<Food>> getAllFoods(@RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(foodService.getAllFoods(keyword));
    }

    @GetMapping("/all/by-session")
    public ResponseEntity<List<Food>> getAllFoodsBySessionTime() {
        return ResponseEntity.ok(foodService.getAllFoodsBySessionTime());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Food> createFood(@ModelAttribute FoodRequest foodRequest) {
        return ResponseEntity.ok(foodService.createFood(foodRequest));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Food> updateFood(@ModelAttribute FoodRequest foodRequest,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(foodService.updateFood(foodRequest, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFood(id));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(ResponseMessage.builder().status(HttpStatus.OK.value()).message("Xóa món ăn thành công").build());
    }
}
