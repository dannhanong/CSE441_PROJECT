package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.events.EventCreateOrder;
import com.ktpm1.restaurant.dtos.events.EventHistoryFoodCreate;
import com.ktpm1.restaurant.repositories.CartRepository;
import com.ktpm1.restaurant.repositories.OrderRepository;
import com.ktpm1.restaurant.services.FoodHistoryService;
import com.ktpm1.restaurant.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodHistoryService foodHistoryService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "food-view")
    public void listenFoodView(EventHistoryFoodCreate message) {
        foodHistoryService.addFoodToHistory(message.getFood(), message.getUser());
    }

    @KafkaListener(topics = "create-order")
    public void listenCreateOrder(EventCreateOrder message) {
        orderRepository.save(message.getOrder());
        cartRepository.delete(message.getCart());
    }
}
