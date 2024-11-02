package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.DishAdapter;
import com.ktpm1.restaurant.adapters.SelectedTableAdapter;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.models.Catalog;
import com.ktpm1.restaurant.models.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewOrderActivity extends AppCompatActivity {

    private RecyclerView rvDishes, rvTables;
    private TextView tvTotalAmount;
    private Button btnConfirmOrder;
    private List<TableResponse> selectedTables;
    List<Order> orders;
    private OrderApi orderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        // Khởi tạo các thành phần giao diện
        rvDishes = findViewById(R.id.rvDishes);
        rvTables = findViewById(R.id.rvTables);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Khởi tạo Retrofit và OrderApi
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.110.68") //
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        orderApi = retrofit.create(OrderApi.class);

        String token = "Bearer " + getToken();

//        fetchReservedTables(token);

        List<String> dishList = Arrays.asList("Gà chiên", "Cá kho tộ", "Bún bò Huế");

        rvDishes.setLayoutManager(new LinearLayoutManager(this));
        rvDishes.setAdapter(new DishAdapter(dishList));

        btnConfirmOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ReviewOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwt_token", "");
    }

}
