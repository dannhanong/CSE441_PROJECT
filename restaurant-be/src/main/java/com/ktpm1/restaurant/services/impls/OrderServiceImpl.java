package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.events.EventCreateOrder;
import com.ktpm1.restaurant.dtos.request.BookingTableRequest;
import com.ktpm1.restaurant.dtos.request.OrderRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.dtos.response.VNPayMessage;
import com.ktpm1.restaurant.models.*;
import com.ktpm1.restaurant.repositories.*;
import com.ktpm1.restaurant.services.BookingTableService;
import com.ktpm1.restaurant.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository oderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private BookingTableRepository bookingTableRepository;
    @Autowired
    private HistoryBookingTableRepository historyBookingTableRepository;
    @Autowired
    private BookingTableService bookingTableService;

    @Override
    public EventCreateOrder createOrder(String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        List<BookingTable> bookingTables = bookingTableRepository.findByUserAndPaidFalseAndAddCartTrue(user);

        if (cart == null || cart.getCartItems().isEmpty()) {
            return null;
        }

        List<Order> orders = new ArrayList<>();

        for (BookingTable bookingTable : bookingTables) {
            Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.CREATED);
            order.setTable(bookingTable.getTable());
            order.setOrderTime(LocalDateTime.now());
            order.setTotalPrice(cart.getTotalPrice());
            bookingTable.setPaid(true);
            bookingTableRepository.save(bookingTable);

            for (CartItem cartItem : cart.getCartItems()) {
                OrderItem orderItem = OrderItem.builder()
                        .food(cartItem.getFood())
                        .quantity(cartItem.getQuantity())
                        .itemPrice(cartItem.getPrice())
                        .order(order)
                        .build();
                order.getOrderItems().add(orderItem);
            }

            orders.add(oderRepository.save(order));
        }
        return EventCreateOrder.builder()
                .orders(orders)
                .cart(cart).build();
    }

    @Override
    public Order createOrderFoodOnly(String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);

        if (cart == null || cart.getCartItems().isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .food(cartItem.getFood())
                    .quantity(cartItem.getQuantity())
                    .itemPrice(cartItem.getPrice())
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        oderRepository.save(order);
        cartRepository.delete(cart);
        return order;
    }

    @Override
    public List<Order> createOrderTableOnly(String username) {
//        User user = userRepository.findByUsername(username);
//        Order order = new Order();
//        order.setUser(user);
//        order.setStatus(OrderStatus.CREATED);
//        order.setOrderTime(LocalDateTime.now());
//        order.setTotalPrice(0);
//        oderRepository.save(order);
//        return order;
        User user = userRepository.findByUsername(username);
        List<BookingTable> bookingTables = bookingTableRepository.findByUserAndAddCartFalse(user);

        List<Order> orders = new ArrayList<>();
        for (BookingTable bookingTable : bookingTables) {
            Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.CREATED);
            order.setTable(bookingTable.getTable());
            order.setOrderTime(LocalDateTime.now());
            order.setTotalPrice(0);

            historyBookingTableRepository.save(HistoryBookingTable.builder()
                    .id(bookingTable.getId())
                    .table(bookingTable.getTable())
                    .user(bookingTable.getUser())
                    .bookingTime(bookingTable.getBookingTime())
                    .startTime(bookingTable.getStartTime())
                    .endTime(bookingTable.getEndTime())
                    .paid(bookingTable.isPaid())
                    .updatedAvailableStart(false)
                    .updatedAvailableEnd(false)
                    .build());

            Order newOrder = oderRepository.save(order);
            orders.add(newOrder);
        }
//        bookingTableRepository.deleteAll(bookingTables);
        return orders;
    }

    @Override
    public Order getOrderById(Long id) {
        return oderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Order> getAllOrder(Pageable pageable, Instant start, Instant end) {
        if (start != null && end != null) {
            return oderRepository.findByOrderTimeBetween(pageable, start, end);
        }
        return oderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> getMyOrder(Pageable pageable, String username) {
        return oderRepository.findByUser(pageable, userRepository.findByUsername(username));
    }

    @Override
    public Order updateOrder(Order order, Long id) {
        return oderRepository.findById(id)
                .map(order1 -> {
                    order1.setTable(order.getTable());
                    order1.setOrderTime(order.getOrderTime());
                    order1.setTotalPrice(order.getTotalPrice());
                    order1.setStatus(order.getStatus());
                    return oderRepository.save(order1);
                })
                .orElse(null);
    }

    @Override
    public ResponseMessage deleteOrder(Long id) {
        oderRepository.deleteById(id);
        return ResponseMessage.builder().status(200).message("Xóa đặt trước thành công").build();
    }

    @Override
    public List<Table> getAvailableTables(Instant start, Instant end) {
        // Lấy danh sách các bàn đã được đặt và lưu vào Set để tối ưu hóa tìm kiếm
//        Set<Table> tableOrdereds = oderRepository.findByOrderTimeBetweenOrEndTimeBetween(start, end, start, end)
//                .stream()
//                .map(Order::getTable)
//                .collect(Collectors.toSet());
//
//        // Lấy toàn bộ danh sách các bàn
//        List<Table> tables = tableRepository.findAll();
//
//        // Tạo danh sách kết quả và cập nhật trạng thái của các bàn
//        List<Table> result = new ArrayList<>();
//        for (Table table : tables) {
//            if (tableOrdereds.contains(table)) {
//                table.setAvailable(false); // Bàn đã được đặt
//            } else {
//                table.setAvailable(true); // Bàn còn trống
//            }
//            result.add(table);
//        }
//
//        return result;
        return null;
    }

    private OrderItem fromCartItemToOrderItem(CartItem cartItem, Order order) {
        return OrderItem.builder()
                .food(cartItem.getFood())
                .quantity(cartItem.getQuantity())
                .itemPrice(cartItem.getPrice())
                .order(order)
                .build();
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public void updateOrderPaid(Long id) {
        oderRepository.findById(id)
                .ifPresent(order -> {
                    order.setPaid(true);
                    oderRepository.save(order);
                });
    }

    @Override
    public EventCreateOrder createOrderTableAndFood(String username, BookingTableRequest bookingTableRequest) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);

        if (cart == null || cart.getCartItems().isEmpty()) {
            return null;
        }

        bookingTableService.createBookingTableBeforeCreateOrderTAndF(username, bookingTableRequest);

        List<Order> orders = new ArrayList<>();

        for(Long tableId : bookingTableRequest.getTableIds()) {
            Table table = tableRepository.findById(tableId).orElse(null);
            Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.CREATED);
            order.setTable(table);
            order.setOrderTime(LocalDateTime.now());
            order.setTotalPrice(cart.getTotalPrice());

            for (CartItem cartItem : cart.getCartItems()) {
                OrderItem orderItem = OrderItem.builder()
                        .food(cartItem.getFood())
                        .quantity(cartItem.getQuantity())
                        .itemPrice(cartItem.getPrice())
                        .order(order)
                        .build();
                order.getOrderItems().add(orderItem);
            }

            orders.add(oderRepository.save(order));
        }

        return EventCreateOrder.builder()
                .orders(orders)
                .cart(cart).build();
    }

}
