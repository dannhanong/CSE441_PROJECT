package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Order {
    Long id;
    User user;
    Table table;
    LocalDateTime orderTime;
    int totalPrice;
    OrderStatus status;
    Set<OrderItem> orderItems = new HashSet<>();
    boolean paid = false;
    LocalDateTime createdAt;
}

