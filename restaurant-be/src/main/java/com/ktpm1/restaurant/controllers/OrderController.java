package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.OrderRequest;
import com.ktpm1.restaurant.dtos.response.VNPayMessage;
import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.OrderService;
import com.ktpm1.restaurant.services.impls.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/admin/all")
    public ResponseEntity<Page<Order>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "orderTime") String sortBy,
                                                    @RequestParam(defaultValue = "desc") String order,
                                                    @RequestParam(defaultValue = "") Instant start,
                                                    @RequestParam(defaultValue = "") Instant end) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortBy));
        return ResponseEntity.ok(orderService.getAllOrder(pageable, start, end));
    }

    @GetMapping("/create")
    public ResponseEntity<List<Table>> getAllTablesStatus(@RequestParam(defaultValue = "") Instant start,
                                                          @RequestParam(defaultValue = "") Instant end){
        return ResponseEntity.ok(orderService.getAvailableTables(start, end));
    }

    @PostMapping("/create")
    public ResponseEntity<VNPayMessage> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);

        long totalPayment = orderService.createOrder(orderRequest, username).getOrder().getTotalPrice();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder((int) totalPayment, "Thanh toán đơn đặt món", baseUrl);

        VNPayMessage VNPayMessage = new VNPayMessage("payment", vnpayUrl);
        return ResponseEntity.ok(VNPayMessage);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<Order>> getMyOrders(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "orderTime") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String order,
                                                   HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortBy));
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(orderService.getMyOrder(pageable, username));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
