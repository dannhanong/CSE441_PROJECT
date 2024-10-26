package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.services.OrderService;
import com.ktpm1.restaurant.services.impls.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/payment")
    public void returnPayment(HttpServletRequest request, Model model, HttpServletResponse response){
        {
            int paymentStatus = vnPayService.orderReturn(request);

            String orderInfo = request.getParameter("vnp_OrderInfo");
            String paymentTime = request.getParameter("vnp_PayDate");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String totalPrice = request.getParameter("vnp_Amount");

            int totalPriceInt = Integer.parseInt(totalPrice);
            totalPriceInt = totalPriceInt / 100;
            totalPrice = String.valueOf(totalPriceInt);

            model.addAttribute("orderId", orderInfo);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("paymentTime", paymentTime);
            model.addAttribute("transactionId", transactionId);
//
            if (paymentStatus == 1) {
                orderService.updateOrderPaid(Long.parseLong(orderInfo));
                try {
                    response.sendRedirect("myapp://payment/success?orderId=" + orderInfo + "&totalPrice=" + totalPrice);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                try {
                    response.sendRedirect("myapp://payment/failure");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
