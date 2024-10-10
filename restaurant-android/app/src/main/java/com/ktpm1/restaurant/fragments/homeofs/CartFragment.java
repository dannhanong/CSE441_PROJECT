package com.ktpm1.restaurant.fragments.homeofs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.CartAdapter;
import com.ktpm1.restaurant.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdateListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    private TextView totalFoodPriceTextView;
    private TextView grandTotalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        totalFoodPriceTextView = view.findViewById(R.id.tv_total_food_price);
        grandTotalTextView = view.findViewById(R.id.tv_grand_total);

        // Khởi tạo danh sách sản phẩm
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Mì tôm", "30000", 3)); // Sử dụng giá dưới dạng chuỗi số không có ký tự "đ"

        // Khởi tạo adapter và truyền vào listener để nhận cập nhật giỏ hàng
        cartAdapter = new CartAdapter(cartItems, getContext(), this);
        recyclerViewCart.setAdapter(cartAdapter);

        // Cập nhật tổng cộng
        updateTotalPrice();

        return view;
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
        totalFoodPriceTextView.setText(totalFoodPrice + "đ");

        // Giá trị phụ phí
        int flowerPrice = 12000;  // Ví dụ giá trị của phụ phí (lọ hoa)
        int totalPrice = totalFoodPrice + flowerPrice;

        // Cập nhật tổng cộng
        grandTotalTextView.setText("Tổng cộng: " + totalPrice + "đ");
    }
}
