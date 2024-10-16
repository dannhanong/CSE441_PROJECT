package com.ktpm1.restaurant.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
