package com.ktpm1.restaurant.schedules;

import com.ktpm1.restaurant.models.HistoryBookingTable;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.repositories.HistoryBookingTableRepository;
import com.ktpm1.restaurant.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingTableScheduler {
    @Autowired
    private HistoryBookingTableRepository historyBookingTableRepository;
    @Autowired
    private TableRepository tableRepository;

    @Scheduled(fixedRate = 60000)
    public void updateTableAvailability() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<HistoryBookingTable> bookings = historyBookingTableRepository.findByStartTimeBeforeAndUpdatedAvailableStartFalse(currentTime);

        for (HistoryBookingTable booking : bookings) {
            Table table = booking.getTable();
            table.setAvailable(false);
            tableRepository.save(table);
            booking.setUpdatedAvailableStart(true);
            historyBookingTableRepository.save(booking);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateTableUnAvailability() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<HistoryBookingTable> bookings = historyBookingTableRepository.findByEndTimeBeforeAndUpdatedAvailableEndFalse(currentTime);

        for (HistoryBookingTable booking : bookings) {
            Table table = booking.getTable();
            table.setAvailable(true);
            tableRepository.save(table);
            booking.setUpdatedAvailableEnd(true);
            historyBookingTableRepository.save(booking);
        }
    }
}
