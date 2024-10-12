package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.FoodHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food-histories")
public class FoodHistoryController {
    @Autowired
    private FoodHistoryService foodHistoryService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/my-food-history")
    public ResponseEntity<List<Food>> getMyFoodHistory(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(foodHistoryService.getFoodHistory(username));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
