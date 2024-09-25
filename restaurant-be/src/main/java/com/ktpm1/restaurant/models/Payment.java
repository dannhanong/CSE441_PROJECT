package com.ktpm1.restaurant.models;

import java.time.Instant;

public class Payment {
    private Long id;
    private Long orderId;
    private long amount;
    private String paymentMethod; // Ví dụ: Tiền mặt, Thẻ tín dụng
    private Instant paymentTime;
}

