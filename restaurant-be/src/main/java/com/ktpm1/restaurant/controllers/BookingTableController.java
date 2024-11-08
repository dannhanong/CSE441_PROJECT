package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.BookingTableRequest;
import com.ktpm1.restaurant.dtos.request.BookingTableUpdateRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.dtos.response.TableResponse;
import com.ktpm1.restaurant.models.BookingTable;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.BookingTableService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/booking-table")
public class BookingTableController {
    @Autowired
    private BookingTableService bookingTableService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createBookingTable(HttpServletRequest request,
                                                           @RequestBody BookingTableRequest bookingTableRequest) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(bookingTableService.createBookingTable(username, bookingTableRequest));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAvailableTables(@RequestParam Long tableId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                        @RequestParam(defaultValue = "0") int additionalTime) {
        return ResponseEntity.ok(bookingTableService.isTableAvailable(tableId, start, additionalTime));
    }

    @GetMapping("/status")
    public ResponseEntity<List<TableResponse>> showStatusTable(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                               @RequestParam(defaultValue = "0") int additionalTime,
                                                               @RequestParam Long catalogId) {
        return ResponseEntity.ok(bookingTableService.showStatusTableByAvailable(start, additionalTime, catalogId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBookingTable(HttpServletRequest request, @PathVariable Long id,
                                                @RequestBody BookingTableUpdateRequest bookingTableRequest) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(bookingTableService.updateBooking(username, bookingTableRequest, id));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ResponseMessage> cancelBooking(HttpServletRequest request, @PathVariable Long id) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(bookingTableService.cancelBooking(username, id));
    }

    @PostMapping("/manage/booking")
    public ResponseEntity<ResponseMessage> manageBooking(@RequestBody BookingTableRequest bookingTableRequest) {
        return ResponseEntity.ok(bookingTableService.createBookingTableByEmployeeRole(bookingTableRequest));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
