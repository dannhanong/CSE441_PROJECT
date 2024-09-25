package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT f FROM Food f WHERE CONCAT(f.name, ' ', f.category.name) LIKE %:keyword%" )
    Page<Food> getAllFood(Pageable pageable, String keyword);
}
