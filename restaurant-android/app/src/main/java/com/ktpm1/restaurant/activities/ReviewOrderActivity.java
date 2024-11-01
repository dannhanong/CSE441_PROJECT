package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.DishAdapter;
import com.ktpm1.restaurant.adapters.SelectedTableAdapter;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.models.Catalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewOrderActivity extends AppCompatActivity {

    private RecyclerView rvDishes, rvTables;
    private TextView tvTotalAmount;
    private Button btnConfirmOrder;
    private List<TableResponse> selectedTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        // Khởi tạo các thành phần giao diện
        rvDishes = findViewById(R.id.rvDishes);
        rvTables = findViewById(R.id.rvTables);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Dữ liệu mẫu cho món ăn
        List<String> dishList = Arrays.asList("Gà chiên", "Cá kho tộ", "Bún bò Huế");

        // Dữ liệu mẫu cho bàn đã chọn
        selectedTables = new ArrayList<>();
        selectedTables.add(new TableResponse(1L, "Bàn số 1", 4, true, new Catalog("Khu A"), true));
        selectedTables.add(new TableResponse(2L, "Bàn số 2", 6, true, new Catalog("Khu B"), true));

        // Thiết lập RecyclerView cho món ăn
        rvDishes.setLayoutManager(new LinearLayoutManager(this));
        rvDishes.setAdapter(new DishAdapter(dishList)); // Sử dụng DishAdapter để hiển thị danh sách món ăn

        // Thiết lập RecyclerView cho bàn đã chọn
        rvTables.setLayoutManager(new LinearLayoutManager(this));
        rvTables.setAdapter(new SelectedTableAdapter(selectedTables)); // Sử dụng SelectedTableAdapter mới

        // Hiển thị tổng giá trị đơn hàng (giả lập)
        tvTotalAmount.setText("500,000 VND");

        // Xử lý sự kiện nút để quay lại MainActivity
        btnConfirmOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ReviewOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng ReviewOrderActivity nếu không cần quay lại
        });
    }
}
