package com.ktpm1.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodDefault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    PaymentMethod paymentMethodEnum;
    @ManyToMany(mappedBy = "paymentMethodDefaults")
    @JsonIgnore
    Set<User> users = new HashSet<>();
}
