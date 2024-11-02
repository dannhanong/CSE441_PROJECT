package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderItemService {
    List<Food> findTop5ByOrderByQuantityDesc(PageRequest pageRequest);
}
