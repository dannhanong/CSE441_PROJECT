package com.ktpm1.restaurant.models;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Catalog {
    Long id;
    String name;
    String description;
    String image;
    Set<Table> tables;
}
