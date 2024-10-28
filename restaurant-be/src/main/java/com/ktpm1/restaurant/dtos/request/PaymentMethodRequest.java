package com.ktpm1.restaurant.dtos.request;

import com.ktpm1.restaurant.models.PaymentMethodDefault;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {
    private Set<PaymentMethodDefault> paymentMethodDefaults;
}
