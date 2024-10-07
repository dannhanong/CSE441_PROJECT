package com.ktpm1.restaurant.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String username;
    private String password;
    private boolean enabled;
    private String verificationCode;
    private String resetPasswordToken;
    private String email;
    private String phoneNumber;
    Set<Role> roles = new HashSet<>();

    private String avatarCode;
}
