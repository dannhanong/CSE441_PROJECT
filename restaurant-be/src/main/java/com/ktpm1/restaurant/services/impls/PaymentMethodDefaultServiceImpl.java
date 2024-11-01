package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.PaymentMethodDefault;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.PaymentMethodDefaultRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.PaymentMethodDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PaymentMethodDefaultServiceImpl implements PaymentMethodDefaultService {
    @Autowired
    private PaymentMethodDefaultRepository paymentMethodDefaultRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseMessage addPaymentMethodDefault(String username, Set<PaymentMethodDefault> paymentMethodDefaults) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseMessage.builder()
                    .status(404)
                    .message("user_not_found")
                    .build();
        }

        user.getPaymentMethodDefaults().addAll(paymentMethodDefaults);
        userRepository.save(user);

        return ResponseMessage.builder()
                .status(200)
                .message("success")
                .build();
    }
}
