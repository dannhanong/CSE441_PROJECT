package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/top-5")
    public ResponseEntity<List<Food>> getTop5Foods() {
        return ResponseEntity.ok(orderItemService.findTop5ByOrderByQuantityDesc(PageRequest.of(0, 5)));
    }
}
