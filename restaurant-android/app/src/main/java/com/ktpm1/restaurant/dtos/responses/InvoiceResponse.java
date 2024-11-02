package com.ktpm1.restaurant.dtos.responses;

public class InvoiceResponse {

    private String invoiceCode;   // Mã hóa đơn
    private String date;          // Ngày lập hóa đơn
    private double totalAmount;   // Tổng số tiền của hóa đơn

    // Constructor không tham số
    public InvoiceResponse(String number, String id, String s, int i) {}

    // Constructor có tham số
    public InvoiceResponse(String invoiceCode, String date, double totalAmount) {
        this.invoiceCode = invoiceCode;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    // Getter và Setter cho các trường
    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean getInvoiceId() {
        return false;
    }
}
