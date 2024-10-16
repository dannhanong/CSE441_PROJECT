package com.ktpm1.restaurant.dtos.response;

import com.ktpm1.restaurant.models.Food;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDetailAndRelated {
    private Food food;
    private List<Food> relatedFoods;
}
