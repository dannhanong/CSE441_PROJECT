package com.ktpm1.restaurant.schedules;

import com.ktpm1.restaurant.models.*;
import com.ktpm1.restaurant.repositories.BookingTableRepository;
import com.ktpm1.restaurant.repositories.HistoryBookingTableRepository;
import com.ktpm1.restaurant.repositories.OrderRepository;
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
    private BookingTableRepository bookingTableRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    public void updateTableUnAvailability() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<BookingTable> bookings = bookingTableRepository.findByEndTimeBeforeAndUpdatedFalse(currentTime);

        for (BookingTable booking : bookings) {
            booking.setUpdated(true);
            bookingTableRepository.save(booking);
        }
    }
}
