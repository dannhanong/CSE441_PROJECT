package com.ktpm1.restaurant.dtos.events;

import com.ktpm1.restaurant.models.BookingTable;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateOrder {
    private List<Order> orders;
    private Cart cart;
}
