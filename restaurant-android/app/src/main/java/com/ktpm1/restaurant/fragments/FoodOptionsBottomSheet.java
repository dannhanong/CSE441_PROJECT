package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.CartApi;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.CartRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.Food;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodOptionsBottomSheet extends BottomSheetDialogFragment {
    private Long foodId;
    private Button btnAddToCart;
    private Button btnEncrease;
    private Button btnDecrease;
    private TextView tvquantity;
    private TextView tvFoodName;
    private TextView tvFoodPrice;
    private int price;

    // Constructor để nhận món ăn đã chọn
    public FoodOptionsBottomSheet(Long foodId) {
        this.foodId = foodId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_options, container, false);

        btnAddToCart = view.findViewById(R.id.addToCartButton);
        btnEncrease = view.findViewById(R.id.quantity_increase);
        btnDecrease = view.findViewById(R.id.quantity_decrease);
        tvquantity = view.findViewById(R.id.quantity_value);
        tvFoodName = view.findViewById(R.id.food_name);
        tvFoodPrice = view.findViewById(R.id.food_price);

        fetchFoodDetail();
        tvFoodPrice.setText(String.valueOf(price));
        btnDecrease.setEnabled(false);

        // Xử lý sự kiện khi người dùng ấn nút tăng số lượng
        btnEncrease.setOnClickListener(v -> {
            if (!btnDecrease.isEnabled()) {
                btnDecrease.setEnabled(true);
            }
            int quantity = Integer.parseInt(tvquantity.getText().toString());
            quantity++;
            int foodPrice = price * quantity;
            tvquantity.setText(String.valueOf(quantity));
            tvFoodPrice.setText(String.valueOf(foodPrice));
        });

        // Xử lý sự kiện khi người dùng ấn nút giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvquantity.getText().toString());
            if (quantity > 2) {
                quantity--;
                int foodPrice = price * quantity;
                tvquantity.setText(String.valueOf(quantity));
                tvFoodPrice.setText(String.valueOf(foodPrice));
            } else {
                quantity--;
                btnDecrease.setEnabled(false);
                tvquantity.setText(String.valueOf(quantity));
                tvFoodPrice.setText(String.valueOf(price));
            }
        });

        btnAddToCart.setOnClickListener(e -> {
            addToCart();
        });

        return view;
    }

    private void fetchFoodDetail() {
        // Gọi API để lấy thông tin chi tiết món ăn
        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);
        Call<Food> call = foodApi.getFoodById(foodId);
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    Food food = response.body();
                    tvFoodName.setText(food.getName());
                    tvFoodPrice.setText(String.valueOf(food.getPrice()));
                    price = food.getPrice();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void addToCart() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        CartApi cartApi = ApiClient.getClient().create(CartApi.class);
        Call<ResponseMessage> call = cartApi.addToCart("Bearer " + token,
                new CartRequest(foodId, Integer.parseInt(tvquantity.getText().toString())));
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage != null) {
                        Toast.makeText(getContext(), responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}