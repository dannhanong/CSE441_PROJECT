package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    Table findByTableNumber(String tableNumber);
    List<Table> findByAvailable(boolean available);
}
