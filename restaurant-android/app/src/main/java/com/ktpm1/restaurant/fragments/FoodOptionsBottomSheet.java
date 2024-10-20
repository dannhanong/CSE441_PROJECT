package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TabStopSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.CartApi;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.apis.OptionApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.CartRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodOption;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodOptionsBottomSheet extends BottomSheetDialogFragment {
    private Long foodId;
    private Button btnAddToCart;
    private ImageButton btnEncrease;
    private ImageButton btnDecrease;
    private TextView tvquantity;
    private TextView tvFoodName;
    private TextView tvFoodPrice;
    LinearLayout linearLayout;
    private int price;
    private ImageView imgFood;
    private List<Long> foodOptionIds = new ArrayList<>();

    // Constructor để nhận món ăn đã chọn
    public FoodOptionsBottomSheet(Long foodId) {
        this.foodId = foodId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_options, container, false);
        init(view);

        fetchFoodDetail();
        tvFoodPrice.setText(String.valueOf(price));
        btnDecrease.setEnabled(false);
        btnDecrease.setImageResource(R.drawable.ic_decrease);

        // Xử lý sự kiện khi người dùng ấn nút tăng số lượng
        btnEncrease.setOnClickListener(v -> {
            if (!btnDecrease.isEnabled()) {
                btnDecrease.setEnabled(true);
                btnDecrease.setImageResource(R.drawable.remove_circle);
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
                btnDecrease.setImageResource(R.drawable.ic_decrease);
                btnDecrease.setEnabled(false);
                tvquantity.setText(String.valueOf(quantity));
                tvFoodPrice.setText(String.valueOf(price));
            }
        });

        fetchFoodOptions();

        btnAddToCart.setOnClickListener(e -> {
            addToCart();
        });

        return view;
    }

    private void fetchFoodDetail() {
        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);
        Call<Food> call = foodApi.getFoodById(foodId);
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    Food food = response.body();
                    tvFoodName.setText(food.getName());
                    tvFoodPrice.setText(String.valueOf(food.getPrice()));
                    String fileCode = food.getImageCode();
                    String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;
                    Glide.with(getContext()).load(imageUrl).into(imgFood);
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

        if (token == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        CartApi cartApi = ApiClient.getClient().create(CartApi.class);
        Call<ResponseMessage> call = cartApi.addToCart("Bearer " + token,
                new CartRequest(foodId, Integer.parseInt(tvquantity.getText().toString()), foodOptionIds));
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

    private void fetchFoodOptions() {
        OptionApi optionApi = ApiClient.getClient().create(OptionApi.class);
        Call<List<FoodOption>> call = optionApi.getOptionsByFood(foodId);
        call.enqueue(new Callback<List<FoodOption>>() {
            @Override
            public void onResponse(Call<List<FoodOption>> call, Response<List<FoodOption>> response) {
                List<FoodOption> foodOptionSelected = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<FoodOption> foodOptions = response.body();
                    for (FoodOption foodOption : foodOptions) {
                        CheckBox checkBox = new CheckBox(getContext());
                        checkBox.setButtonTintList(getResources().getColorStateList(R.color.blue_bg));
//                        checkBox.setText(foodOption.getName() + " - " + foodOption.getPrice() + "đ");
                        setupCheckBox(checkBox, foodOption);
                        linearLayout.addView(checkBox);

                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                foodOptionIds.add(foodOption.getId());
                                foodOptionSelected.add(foodOption);
                                checkBoxChange(foodOptionSelected, price);
                            } else {
                                foodOptionIds.remove(foodOption.getId());
                                foodOptionSelected.remove(foodOption);
                                checkBoxChange(foodOptionSelected, price);
                            }
                        });
                    }

                    quantityChange(foodOptionSelected, price);
                }
            }

            @Override
            public void onFailure(Call<List<FoodOption>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void quantityChange(List<FoodOption> foodOptions, int foodPrice) {
        tvquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int totalPrice = foodPrice * Integer.parseInt(tvquantity.getText().toString()) + foodOptions.stream().mapToInt(FoodOption::getPrice).sum() * Integer.parseInt(tvquantity.getText().toString());
                    btnAddToCart.setText("Thêm vào giỏ hàng" + " - " + totalPrice + "đ");
                } catch (NumberFormatException e) {
                    // Xử lý nếu giá trị trong tvquantity hoặc tvFoodPrice không phải là số
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkBoxChange(List<FoodOption> foodOptions, int foodPrice) {
        int quantity = Integer.parseInt(tvquantity.getText().toString());
        int totalPrice = foodPrice * quantity
                + foodOptions.stream().mapToInt(FoodOption::getPrice).sum() * quantity;
        btnAddToCart.setText("Thêm vào giỏ hàng" + " - " + totalPrice + "đ");
    }

    public void setupCheckBox(CheckBox checkBox, FoodOption foodOption) {
        String foodName = foodOption.getName();
        String foodPrice = foodOption.getPrice() + "đ";

        int maxNameLength = 30;
        int numSpaces = maxNameLength - foodName.length();
        if (numSpaces < 0) numSpaces = 0;

        // Tạo chuỗi khoảng trắng
        String spaces = new String(new char[numSpaces]).replace('\0', ' ');

        // Tạo SpannableString với tên món ăn + khoảng trắng + giá
        SpannableString spannableString = new SpannableString(foodName + spaces + foodPrice);

        checkBox.setText(spannableString);
    }

    public void init(View view) {
        btnAddToCart = view.findViewById(R.id.addToCartButton);
        btnEncrease = view.findViewById(R.id.quantity_increase);
        btnDecrease = view.findViewById(R.id.quantity_decrease);
        tvquantity = view.findViewById(R.id.quantity_value);
        tvFoodName = view.findViewById(R.id.food_name);
        tvFoodPrice = view.findViewById(R.id.food_price);
        linearLayout = view.findViewById(R.id.extra_options_list);
        imgFood = view.findViewById(R.id.img_food_image);
    }
}