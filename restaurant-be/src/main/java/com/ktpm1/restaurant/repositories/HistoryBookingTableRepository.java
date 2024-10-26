package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.HistoryBookingTable;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryBookingTableRepository extends JpaRepository<HistoryBookingTable, Long> {
    List<HistoryBookingTable> findByUser(User user);
    List<HistoryBookingTable> findByStartTimeBeforeAndUpdatedAvailableStartFalse(LocalDateTime currentTime);
    List<HistoryBookingTable> findByEndTimeBeforeAndUpdatedAvailableEndFalse(LocalDateTime currentTime);
}
