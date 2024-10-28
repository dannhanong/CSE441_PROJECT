package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.BookingTableRequest;
import com.ktpm1.restaurant.dtos.request.BookingTableUpdateRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.dtos.response.TableResponse;
import com.ktpm1.restaurant.models.BookingTable;
import com.ktpm1.restaurant.models.Table;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingTableService {
    ResponseMessage createBookingTable(String username, BookingTableRequest bookingTableRequest);
    void createBookingTableBeforeCreateOrderTAndF(String username, BookingTableRequest bookingTableRequest);
    ResponseMessage createBookingTableByEmployeeRole(BookingTableRequest bookingTableRequest);
    boolean isTableAvailable(Long tableId, LocalDateTime startTime, int additionalTime);
    List<BookingTable> getBookingsByDate(LocalDateTime date);
    BookingTable updateBooking(String username, BookingTableUpdateRequest bookingTableRequest, Long bookingId);
    List<TableResponse> showStatusTableByAvailable(LocalDateTime startTime, int additionalTime, Long catalogId);
    ResponseMessage cancelBooking(String username, Long bookingId);
}
