package com.ktpm1.restaurant.inits;

import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.RoleName;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.repositories.RoleRepository;
import com.ktpm1.restaurant.repositories.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitDatabase {
    @Bean
    CommandLineRunner initRole(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName(RoleName.ADMIN)) {
                Role adminRole = new Role();
                adminRole.setName(RoleName.ADMIN);
                roleRepository.save(adminRole);
            }
            if (!roleRepository.existsByName(RoleName.USER)) {
                Role userRole = new Role();
                userRole.setName(RoleName.USER);
                roleRepository.save(userRole);
            }
        };
    }

    @Bean
    CommandLineRunner initTable(TableRepository tableRepository) {
        return args -> {
            if (tableRepository.count() == 0) {
                tableRepository.saveAll(
                        List.of(
                                Table.builder().tableNumber("Bàn 1").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 2").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 3").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 4").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 5").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 6").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 7").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 8").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 9").capacity(6).available(true).build(),
                                Table.builder().tableNumber("Bàn 10").capacity(6).available(true).build()
                        )
                );
            }
        };
    }
}
