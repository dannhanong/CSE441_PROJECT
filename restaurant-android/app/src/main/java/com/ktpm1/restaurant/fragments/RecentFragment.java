package com.ktpm1.restaurant.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.FoodHistoryAdapter;
import com.ktpm1.restaurant.apis.FoodHistoryApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Food;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodHistoryAdapter adapter;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerViewFoodHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Thêm tab thủ công
        tabLayout.addTab(tabLayout.newTab().setText("Mới nhất"));
        tabLayout.addTab(tabLayout.newTab().setText("Cũ nhất"));

        loadFoodHistory();

        // Xử lý sự kiện chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Tải lại dữ liệu dựa trên tab được chọn
                loadFoodHistory();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void loadFoodHistory() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        FoodHistoryApi api = ApiClient.getClient().create(FoodHistoryApi.class);

        Call<List<Food>> call;

        if (tabLayout.getSelectedTabPosition() == 0) {
            call = api.getMyFoodHistory("Bearer " + token, "desc");
        } else {
            call = api.getMyFoodHistory("Bearer " + token, "asc");
        }

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Food> foodList = response.body();
                    adapter = new FoodHistoryAdapter(foodList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load food history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
