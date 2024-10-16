package com.ktpm1.restaurant.dtos.events;

import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.Order;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateOrder {
    private Order order;
    private Cart cart;
}
