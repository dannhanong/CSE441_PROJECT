package com.ktpm1.restaurant.models;

public class CartItem {
    private String productName; // Tên của sản phẩm
    private String productPrice; // Giá của sản phẩm dưới dạng String (ví dụ: "30000đ")
    private int quantity; // Số lượng sản phẩm

    // Constructor để khởi tạo CartItem
    public CartItem(String productName, String productPrice, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    // Getter và Setter cho tên sản phẩm
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter và Setter cho giá sản phẩm
    public String getProductPriceString() {
        return productPrice;
    }

    public void setProductPriceString(String productPrice) {
        this.productPrice = productPrice;
    }

    // Lấy giá sản phẩm dưới dạng số nguyên (int)
    public int getBasePrice() {
        try {
            // Loại bỏ ký tự không phải số (vd: dấu chấm hoặc ký tự "đ")
            String cleanedPrice = productPrice.replace(".", "").replace("đ", "").trim();
            return Integer.parseInt(cleanedPrice);
        } catch (NumberFormatException e) {
            // Nếu không thể chuyển đổi, trả về 0 để tránh lỗi
            return 0;
        }
    }

    // Getter và Setter cho số lượng
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Tính tổng giá dựa trên giá cơ bản và số lượng
    public int getTotalPrice() {
        return getBasePrice() * quantity;
    }
}
