package com.ktpm1.restaurant.dtos.events;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventHistoryFoodCreate {
    private Food food;
    private User user;
}
