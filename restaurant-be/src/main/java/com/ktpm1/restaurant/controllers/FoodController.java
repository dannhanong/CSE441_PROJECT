package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.events.EventHistoryFoodCreate;
import com.ktpm1.restaurant.dtos.request.FoodRequest;
import com.ktpm1.restaurant.dtos.response.FoodDetailAndRelated;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.repositories.FoodHistoryRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.FoodService;
import com.ktpm1.restaurant.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foods")
public class FoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

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
    public ResponseEntity<FoodDetailAndRelated> getFood(@PathVariable Long id,
                                                        HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            String username = jwtService.extractUsername(token);
            return ResponseEntity.ok(foodService.getFoodDetailAndRelated(id, username));
        } catch (Exception e) {
            return ResponseEntity.ok(foodService.getFoodDetailAndRelated(id));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(ResponseMessage.builder().status(HttpStatus.OK.value()).message("Xóa món ăn thành công").build());
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
