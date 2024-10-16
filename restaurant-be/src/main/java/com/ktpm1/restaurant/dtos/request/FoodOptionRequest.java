package com.ktpm1.restaurant.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodOptionRequest {
    String name;
    int price;
    Long foodId;
}

