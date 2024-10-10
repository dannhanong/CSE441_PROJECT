package com.ktpm1.restaurant.models;

public class ExtraItem {
    private String name;
    private int price;
    private int quantity;
    private boolean selected;

    // Constructor
    public ExtraItem(String name, int price) {
        this.name = name;
        this.price = price;
        this.quantity = 1; // Mặc định số lượng là 1 khi tạo mới
        this.selected = false; // Mặc định là chưa được chọn
    }

    // Getter cho tên sản phẩm
    public String getName() {
        return name;
    }

    // Getter cho giá gốc của sản phẩm
    public int getPrice() {
        return price;
    }

    // Getter cho số lượng sản phẩm
    public int getQuantity() {
        return quantity;
    }

    // Setter cho số lượng sản phẩm
    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    // Getter cho trạng thái chọn của sản phẩm
    public boolean isSelected() {
        return selected;
    }

    // Setter cho trạng thái chọn của sản phẩm
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Tính tổng giá dựa trên số lượng
    public int getTotalPrice() {
        return price * quantity;
    }

    // Tăng số lượng sản phẩm
    public void increaseQuantity() {
        this.quantity++;
    }

    // Giảm số lượng sản phẩm, đảm bảo không nhỏ hơn 1
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    @Override
    public String toString() {
        return "ExtraItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", selected=" + selected +
                ", total price=" + getTotalPrice() +
                '}';
    }
}
