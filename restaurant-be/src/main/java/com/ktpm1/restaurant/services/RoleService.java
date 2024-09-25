package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.RoleName;

public interface RoleService {
    Role findByName(RoleName name);
}
