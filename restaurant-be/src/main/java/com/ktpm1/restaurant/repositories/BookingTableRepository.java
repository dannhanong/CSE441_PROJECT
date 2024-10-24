package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.BookingTable;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingTableRepository extends JpaRepository<BookingTable, Long> {
    List<BookingTable> findAllByTableId(Long tableId);
    List<BookingTable> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByTableAndStartTimeBetweenAndUpdatedFalse(Table table, LocalDateTime start, LocalDateTime end);
    boolean existsByTableAndStartTimeLessThanAndEndTimeGreaterThan(Table table, LocalDateTime endTime, LocalDateTime startTime);
    boolean existsByTableAndEndTimeAfterAndStartTimeBeforeAndUpdatedFalse(Table table, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsByTableAndEndTimeAfterAndStartTimeAfter(Table table, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsByTableAndStartTimeBetweenOrEndTimeBetween(Table table, LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2);
    boolean existsByTableAndStartTimeBeforeAndEndTimeAfterAndUpdatedFalse(Table table, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsByTableAndStartTimeBetweenAndIdNotAndUpdatedFalse(Table table, LocalDateTime start, LocalDateTime end, Long id);
    boolean existsByTableAndEndTimeAfterAndStartTimeBeforeAndIdNotAndUpdatedFalse(Table table, LocalDateTime start, LocalDateTime end, Long id);
    boolean existsByTableAndStartTimeBeforeAndEndTimeAfterAndIdNotAndUpdatedFalse(Table table, LocalDateTime start, LocalDateTime end, Long id);
    List<BookingTable> findByStartTimeBeforeAndUpdatedFalse(LocalDateTime currentTime);
    List<BookingTable> findByEndTimeBeforeAndUpdatedFalse(LocalDateTime currentTime);
    List<BookingTable> findByUser(User user);
    List<BookingTable> findByUserAndPaidFalse(User user);
    @Query("SELECT b FROM BookingTable b WHERE b.user.id = :userId AND b.bookingTime = (SELECT MAX(b2.bookingTime) FROM BookingTable b2 WHERE b2.user.id = :userId)")
    List<BookingTable> findLatestBookingsByUserIdAndBookingTime(@Param("userId") Long userId);
}
