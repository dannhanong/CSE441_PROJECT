package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.OrderRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.OrderStatus;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.OrderRepository;
import com.ktpm1.restaurant.repositories.TableRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository oderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest, String username) {
        User user = userRepository.findByUsername(username);
        Order order = Order.builder()
                .table(tableRepository.findById(orderRequest.getTableId()).orElse(null))
                .orderTime(orderRequest.getOrderTime())
                .endTime(orderRequest.getEndTime())
                .totalPrice(100000)
                .status(OrderStatus.SCHEDULED)
                .user(user)
                .build();
        return oderRepository.save(order);
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

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

}
