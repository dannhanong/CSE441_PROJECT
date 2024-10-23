package com.ktpm1.restaurant.dtos.requests;

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
    String startTime;
    int additionalTime = 0;
}
