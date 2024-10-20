package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Table {
    Long id;
    String tableNumber;
    int capacity;
    boolean available;
    Catalog catalog;
}

