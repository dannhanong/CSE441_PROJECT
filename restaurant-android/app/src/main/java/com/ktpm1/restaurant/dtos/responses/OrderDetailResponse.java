package com.ktpm1.restaurant.dtos.responses;

import java.util.List;

public class OrderDetailResponse {

    private String invoiceId;           // Mã hóa đơn
    private List<String> dishes;        // Danh sách các món ăn trong hóa đơn
    private List<TableResponse> tables; // Danh sách các bàn đã đặt
    private double totalAmount;         // Tổng tiền của hóa đơn

    // Constructor không tham số
    public OrderDetailResponse() {}

    // Constructor có tham số
    public OrderDetailResponse(String invoiceId, List<String> dishes, List<TableResponse> tables, double totalAmount) {
        this.invoiceId = invoiceId;
        this.dishes = dishes;
        this.tables = tables;
        this.totalAmount = totalAmount;
    }

    // Getter và Setter cho các trường
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public List<TableResponse> getTables() {
        return tables;
    }

    public void setTables(List<TableResponse> tables) {
        this.tables = tables;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
