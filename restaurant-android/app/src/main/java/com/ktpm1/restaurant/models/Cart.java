package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    Long id;
    User user;
    Set<CartItem> cartItems = new HashSet<>();
    int totalPrice;
}
