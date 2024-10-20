package com.ktpm1.restaurant.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingTable {
    Long id;
    User user;
    Table table;
    LocalDateTime bookingTime;
    LocalDateTime startTime;
    LocalDateTime endTime;
    boolean paid = false;
    boolean updated = false;
}
