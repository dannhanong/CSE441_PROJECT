package com.ktpm1.restaurant.adapters;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartUpdateListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách sản phẩm
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Mì tôm", "30000", 3)); // Sử dụng giá dưới dạng chuỗi số không có ký tự "đ"

        // Khởi tạo adapter và truyền vào listener để nhận cập nhật giỏ hàng
        cartAdapter = new CartAdapter(cartItems, this, this);
        recyclerViewCart.setAdapter(cartAdapter);

        // Cập nhật tổng cộng
        updateTotalPrice();
    }

    @Override
    public void onCartUpdated() {
        // Khi giỏ hàng được cập nhật từ CartAdapter, sẽ gọi hàm này để cập nhật tổng tiền
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        int totalFoodPrice = 0;

        for (CartItem item : cartItems) {
            try {
                int basePrice = item.getBasePrice(); // Sử dụng getBasePrice() đã được sửa của CartItem
                totalFoodPrice += basePrice * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Ghi lại lỗi nếu gặp phải vấn đề khi chuyển đổi giá trị
            }
        }

        // Cập nhật lại tổng tiền trên giao diện
        TextView totalFoodPriceTextView = findViewById(R.id.tv_total_food_price);
        totalFoodPriceTextView.setText(totalFoodPrice + "đ");

        // Giá trị phụ phí
        int flowerPrice = 12000;  // Ví dụ giá trị của phụ phí (lọ hoa)
        int totalPrice = totalFoodPrice + flowerPrice;

        // Cập nhật tổng cộng
        TextView grandTotalTextView = findViewById(R.id.tv_grand_total);
        grandTotalTextView.setText("Tổng cộng: " + totalPrice + "đ");
    }
}
