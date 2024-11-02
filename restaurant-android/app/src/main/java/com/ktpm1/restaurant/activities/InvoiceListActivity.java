package com.ktpm1.restaurant.activities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.InvoiceAdapter;
import com.ktpm1.restaurant.dtos.responses.InvoiceResponse;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.models.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceListActivity extends AppCompatActivity {

    private RecyclerView rvInvoiceList;
    private List<InvoiceResponse> invoiceList;
    private InvoiceAdapter invoiceAdapter;
    private OrderApi orderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list);

        rvInvoiceList = findViewById(R.id.rvInvoiceList);
        invoiceList = new ArrayList<>();
        invoiceAdapter = new InvoiceAdapter(this, invoiceList);
        rvInvoiceList.setLayoutManager(new LinearLayoutManager(this));
        rvInvoiceList.setAdapter(invoiceAdapter);

        // Khởi tạo Retrofit và OrderApi
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.110.68") // Thay bằng URL cơ sở của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderApi = retrofit.create(OrderApi.class);
                getInvoiceList();
    }

    private void getInvoiceList() {
        String token = getToken();

        orderApi.getInvoiceList(token).enqueue(new Callback<List<InvoiceResponse>>() {
            @Override
            public void onResponse(Call<List<InvoiceResponse>> call, Response<List<InvoiceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    invoiceList = response.body();
                    invoiceAdapter.setInvoiceList(invoiceList); // Cập nhật danh sách hóa đơn vào adapter
                    rvInvoiceList.setAdapter(invoiceAdapter); // Đảm bảo adapter được gán lại sau khi có dữ liệu
                    getReservedTables(token); // Lấy danh sách bàn đã đặt
                    getOrderedFood(token); // Lấy danh sách món ăn đã đặt
                } else {
                    Log.e("InvoiceListActivity", "Lỗi khi lấy danh sách hóa đơn: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceResponse>> call, Throwable t) {
                Log.e("InvoiceListActivity", "Lỗi khi gọi API lấy danh sách hóa đơn: " + t.getMessage());
            }
        });
    }


    private void getReservedTables(String token) {
        orderApi.getReservedTables(token).enqueue(new Callback<List<TableResponse>>() {
            @Override
            public void onResponse(Call<List<TableResponse>> call, Response<List<TableResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TableResponse> reservedTables = response.body();
                    // Thêm danh sách bàn vào adapter để hiển thị
                    invoiceAdapter.setReservedTables(reservedTables);
                } else {
                    Log.e("InvoiceListActivity", "Lỗi khi lấy danh sách bàn đã đặt: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TableResponse>> call, Throwable t) {
                Log.e("InvoiceListActivity", "Lỗi khi gọi API lấy danh sách bàn: " + t.getMessage());
            }
        });
    }

    private void getOrderedFood(String token) {
        orderApi.getOrderedFood(token).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderedFood = response.body();
                    // Thêm danh sách món ăn vào adapter để hiển thị
                    invoiceAdapter.setOrderedFood(orderedFood);
                } else {
                    Log.e("InvoiceListActivity", "Lỗi khi lấy danh sách món ăn đã đặt: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("InvoiceListActivity", "Lỗi khi gọi API lấy danh sách món ăn: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}
