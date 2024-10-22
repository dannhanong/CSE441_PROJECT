package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItem {
    Long id;

    Order order;
    Food food;
    int quantity;
    long itemPrice;
}

