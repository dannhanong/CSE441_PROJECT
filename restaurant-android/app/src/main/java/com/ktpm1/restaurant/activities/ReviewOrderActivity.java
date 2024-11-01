package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.DishAdapter;
import com.ktpm1.restaurant.adapters.SelectedTableAdapter;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.apis.RetrofitClient;
import com.ktpm1.restaurant.dtos.requests.BookingTableRequest;
import com.ktpm1.restaurant.dtos.responses.VNPayMessage;
import com.ktpm1.restaurant.models.Catalog;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.dtos.responses.OrderDetailResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewOrderActivity extends AppCompatActivity {

    private RecyclerView rvDishes, rvTables;
    private TextView tvTotalAmount;
    private Button btnConfirmOrder;
    private List<TableResponse> selectedTables = new ArrayList<>();
    private List<String> dishList = new ArrayList<>();
    private OrderApi orderApi;
    private static final String BASE_URL = "http://192.168.110.68:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        // Khởi tạo Retrofit và API
        orderApi = RetrofitClient.getClient(BASE_URL).create(OrderApi.class);

        // Khởi tạo các thành phần giao diện
        rvDishes = findViewById(R.id.rvDishes);
        rvTables = findViewById(R.id.rvTables);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Nhận mã hóa đơn (invoiceId) từ Intent
        String invoiceId = getIntent().getStringExtra("invoiceId");
        if (invoiceId != null) {
            fetchOrderDetails(invoiceId);
        } else {
            Toast.makeText(this, "Không tìm thấy mã hóa đơn", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện khi nhấn nút "Xác nhận đặt hàng"
        btnConfirmOrder.setOnClickListener(view -> createOrderWithFoodAndTable());
    }

    // Lấy chi tiết hóa đơn từ API
    private void fetchOrderDetails(String invoiceId) {
        orderApi.getOrderDetails(invoiceId).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderDetailResponse orderDetail = response.body();

                    // Cập nhật giao diện với dữ liệu từ API
                    dishList = orderDetail.getDishes();
                    selectedTables = orderDetail.getTables();
                    tvTotalAmount.setText(orderDetail.getTotalAmount() + " VND");

                    // Cập nhật RecyclerView cho món ăn
                    rvDishes.setLayoutManager(new LinearLayoutManager(ReviewOrderActivity.this));
                    rvDishes.setAdapter(new DishAdapter(dishList));

                    // Cập nhật RecyclerView cho bàn đã chọn
                    rvTables.setLayoutManager(new LinearLayoutManager(ReviewOrderActivity.this));
                    rvTables.setAdapter(new SelectedTableAdapter(selectedTables));

                } else {
                    Toast.makeText(ReviewOrderActivity.this, "Không lấy được chi tiết hóa đơn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Toast.makeText(ReviewOrderActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrderWithFoodAndTable() {
        BookingTableRequest bookingTableRequest = new BookingTableRequest();
        bookingTableRequest.setDishes(dishList);
        List<Long> tableIds = new ArrayList<>();
        for (TableResponse table : selectedTables) {
            tableIds.add(table.getId());
        }
        bookingTableRequest.setTableIds(tableIds);

        // Gọi API để tạo đơn hàng
        orderApi.createOrderWithFoodAndTable(bookingTableRequest).enqueue(new Callback<VNPayMessage>() {
            @Override
            public void onResponse(Call<VNPayMessage> call, Response<VNPayMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VNPayMessage message = response.body();
                    Toast.makeText(ReviewOrderActivity.this, "Thanh toán thành công! " + message.getPaymentUrl(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewOrderActivity.this, "Lỗi khi tạo đơn hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VNPayMessage> call, Throwable t) {
                Toast.makeText(ReviewOrderActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
