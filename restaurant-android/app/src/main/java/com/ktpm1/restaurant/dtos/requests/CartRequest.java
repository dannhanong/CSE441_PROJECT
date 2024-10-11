package com.ktpm1.restaurant.dtos.requests;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    Long foodId;
    int quantity = 1;
    List<Long> foodOptionIds;
}
