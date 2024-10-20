package com.ktpm1.restaurant.models;

import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Long orderId;
    private long amount;
    private String paymentMethod;
    private LocalDateTime paymentTime;
}

