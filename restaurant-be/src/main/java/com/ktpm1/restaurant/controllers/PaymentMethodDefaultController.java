package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.PaymentMethodRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.PaymentMethodDefault;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.PaymentMethodDefaultService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/payment-method-defaults")
public class PaymentMethodDefaultController {
    @Autowired
    private PaymentMethodDefaultService paymentMethodDefaultService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addPaymentMethodDefault(@RequestBody PaymentMethodRequest paymentMethodRequest,
                                                                   HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        Set<PaymentMethodDefault> paymentMethodDefaults = new HashSet<>(paymentMethodRequest.getPaymentMethodDefaults());
        return ResponseEntity.ok(paymentMethodDefaultService.addPaymentMethodDefault(username, paymentMethodDefaults));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
