package com.ktpm1.restaurant.dtos.response;

import com.ktpm1.restaurant.models.Catalog;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TableResponse {
    Long id;
    String tableNumber;
    int capacity;
    boolean available;
    Catalog catalog;
}
