package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.RoleName;
import com.ktpm1.restaurant.repositories.RoleRepository;
import com.ktpm1.restaurant.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
