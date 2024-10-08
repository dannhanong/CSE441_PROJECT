package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.SessionTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT f FROM Food f WHERE CONCAT(f.name, ' ', f.category.name) LIKE %:keyword%" )
    Page<Food> getAllFood(Pageable pageable, String keyword);

    @Query("SELECT f FROM Food f WHERE CONCAT(f.name, ' ', f.category.name) LIKE %:keyword%" )
    List<Food> getAllFood(String keyword);

    List<Food> findAllBySessionTime(SessionTime sessionTime);
}
