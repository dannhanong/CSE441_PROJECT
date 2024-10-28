package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.PaymentMethodDefault;

import java.util.Set;

public interface PaymentMethodDefaultService {
    ResponseMessage addPaymentMethodDefault(String username, Set<PaymentMethodDefault> paymentMethodDefaults);
}
