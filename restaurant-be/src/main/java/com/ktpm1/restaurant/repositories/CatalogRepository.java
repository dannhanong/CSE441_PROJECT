package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Catalog findByName(String name);
    List<Catalog> findByNameContaining(String name);
}
