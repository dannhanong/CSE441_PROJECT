package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    Long id;
    Cart cart;
    Food food;
    int quantity;
    long price;
    List<FoodOption> options;
}
