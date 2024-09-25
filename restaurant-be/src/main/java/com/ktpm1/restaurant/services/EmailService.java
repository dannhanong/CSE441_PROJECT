package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.models.User;

public interface EmailService {
    public void sendVerificationEmail(User user);
}
