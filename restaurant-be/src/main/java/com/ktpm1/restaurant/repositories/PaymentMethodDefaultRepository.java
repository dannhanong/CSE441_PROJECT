package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.PaymentMethodDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodDefaultRepository extends JpaRepository<PaymentMethodDefault, Long> {
}
