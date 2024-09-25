package com.ktpm1.restaurant.dtos.request;

import com.ktpm1.restaurant.models.OrderStatus;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderRequest {
    Long tableId;
    Instant orderTime;
    Instant endTime;
}
