package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.repositories.OrderItemRepository;
import com.ktpm1.restaurant.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<Food> findTop5ByOrderByQuantityDesc(PageRequest pageRequest) {
        return orderItemRepository.findTop5ByOrderByQuantityDesc(pageRequest).getContent();
    }
}
