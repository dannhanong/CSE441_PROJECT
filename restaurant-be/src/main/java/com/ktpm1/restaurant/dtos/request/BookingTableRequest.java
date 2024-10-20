package com.ktpm1.restaurant.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingTableRequest {
    List<Long> tableIds;
    LocalDateTime startTime;
    int additionalTime = 0;
}
