package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.services.impls.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/payment")
    public ResponseEntity<String> returnPayment(HttpServletRequest request, Model model, HttpSession session) {
        int paymentStatus =vnPayService.orderReturn(request);

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
//        if (paymentStatus == 1){
//            return "ordersuccess";
//        }else
//            return "orderfail";
        return ResponseEntity.ok("ordersuccess");
    }
}
