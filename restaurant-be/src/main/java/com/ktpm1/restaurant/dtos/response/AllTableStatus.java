package com.ktpm1.restaurant.dtos.response;

import com.ktpm1.restaurant.models.Table;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllTableStatus {
    private List<Table> availableTables;
    private List<Table> unavailableTables;
}
