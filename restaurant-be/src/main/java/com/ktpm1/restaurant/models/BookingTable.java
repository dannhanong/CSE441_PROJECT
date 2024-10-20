package com.ktpm1.restaurant.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "booking_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id", nullable = false)
    com.ktpm1.restaurant.models.Table table;
    @Column(nullable = false)
    LocalDateTime bookingTime = LocalDateTime.now();
    @Column(nullable = false)
    LocalDateTime startTime;
    @Column(nullable = false)
    LocalDateTime endTime;
    @Column(nullable = false)
    boolean paid = false;
    boolean updated = false;
}
