package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.FoodAdapter;
import com.ktpm1.restaurant.adapters.ImagePagerAdapter;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.responses.FoodDetailAndRelated;
import com.ktpm1.restaurant.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailFragment extends Fragment {
    private ViewPager2 viewPagerImages;
    private TextView tvDishName, tvDishDescription, tvPageIndicator, tvFoodPrice;
    private Button btnAddToCart;
    private RecyclerView recyclerViewRelatedDishes;
    private FoodDetailAndRelated foodDetailAndRelated;
    private Long foodId;

    public FoodDetailFragment(Long foodId) {
        this.foodId = foodId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);

        // Ánh xạ các view
        viewPagerImages = view.findViewById(R.id.viewPagerImages);
        tvDishName = view.findViewById(R.id.tvDishName);
        tvDishDescription = view.findViewById(R.id.tvDishDescription);
        tvPageIndicator = view.findViewById(R.id.tvPageIndicator);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        recyclerViewRelatedDishes = view.findViewById(R.id.recyclerViewRelatedDishes);
        tvFoodPrice = view.findViewById(R.id.tv_food_price);

        fetchFoodDetails();

        btnAddToCart.setOnClickListener(e -> {
            FoodOptionsBottomSheet bottomSheet = new FoodOptionsBottomSheet(foodId);
            bottomSheet.show(getParentFragmentManager(), "FoodOptionsBottomSheet");
        });

        return view;
    }

    private void fetchFoodDetails() {
        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);

        Call<FoodDetailAndRelated> call = foodApi.getFoodDetailAndRelated(foodId);
        call.enqueue(new Callback<FoodDetailAndRelated>() {
            @Override
            public void onResponse(Call<FoodDetailAndRelated> call, Response<FoodDetailAndRelated> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodDetailAndRelated = response.body();

                    // Hiển thị chi tiết món ăn và các món liên quan
                    displayFoodDetails(foodDetailAndRelated.getFood());
                    setupRelatedDishesRecyclerView(foodDetailAndRelated.getRelatedFoods());
                } else {
                    // Xử lý khi không lấy được dữ liệu
                    Toast.makeText(getContext(), "Không thể tải chi tiết món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodDetailAndRelated> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Lỗi khi tải chi tiết món ăn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayFoodDetails(Food food) {
        tvDishName.setText(food.getName());
        tvFoodPrice.setText(String.format("%,dđ", food.getPrice()));
        tvDishDescription.setText(food.getDescription());

        // Tải ảnh vào ViewPager2, giả sử bạn có danh sách URL hình ảnh trong food.getImages()
        List<String> fileCodes = food.getImageList();
        ImagePagerAdapter adapter = new ImagePagerAdapter(fileCodes);
        viewPagerImages.setAdapter(adapter);

        tvPageIndicator.setText("1/" + fileCodes.size());

        viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Cập nhật chỉ số trang hiện tại khi người dùng thay đổi trang
                tvPageIndicator.setText((position + 1) + "/" + fileCodes.size());
            }
        });
    }

    private void setupRelatedDishesRecyclerView(List<Food> relatedDishes) {
        FoodAdapter adapter = new FoodAdapter(relatedDishes);
        recyclerViewRelatedDishes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRelatedDishes.setAdapter(adapter);
    }
}