package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
    boolean existsByName(RoleName name);
}
