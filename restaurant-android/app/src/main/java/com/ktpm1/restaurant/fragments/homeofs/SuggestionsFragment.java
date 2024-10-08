package com.ktpm1.restaurant.fragments.homeofs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.FoodAdapter;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList = new ArrayList<>();

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFoods);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

        fetchFoodList();

        return view;
    }

    private void fetchFoodList() {
        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);

        Call<List<Food>> call = foodApi.getAllFoodsBySession();
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    foodList = response.body();
                    foodAdapter.setFoodList(foodList);
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}