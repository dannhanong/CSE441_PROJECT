package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.events.EventCreateOrder;
import com.ktpm1.restaurant.dtos.request.BookingTableRequest;
import com.ktpm1.restaurant.dtos.request.OrderRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.dtos.response.VNPayMessage;
import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface OrderService {
    EventCreateOrder createOrder(String username);
    Order createOrderFoodOnly(String username);
    List<Order> createOrderTableOnly(String username);
    Order getOrderById(Long id);
    Page<Order> getAllOrder(Pageable pageable, Instant start, Instant end);
    Page<Order> getMyOrder(Pageable pageable, String username);
    Order updateOrder(Order order, Long id);
    ResponseMessage deleteOrder(Long id);
    List<Table> getAvailableTables(Instant start, Instant end);
    List<Table> getAllTables();
    void updateOrderPaid(Long id);
    EventCreateOrder createOrderTableAndFood(String username, BookingTableRequest bookingTableRequest);
}
