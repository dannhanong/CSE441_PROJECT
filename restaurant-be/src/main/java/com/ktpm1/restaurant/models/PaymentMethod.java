package com.ktpm1.restaurant.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PaymentMethod {
    CASH,
    VN_PAY
}
