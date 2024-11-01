package com.ktpm1.restaurant.models;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "history_booking_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryBookingTable {
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
    LocalDateTime bookingTime;
    LocalDateTime startTime;
    @Column(nullable = false)
    LocalDateTime endTime;
    @Column(nullable = false)
    boolean paid = false;
    boolean updatedAvailableStart = false;
    boolean updatedAvailableEnd = false;
}
