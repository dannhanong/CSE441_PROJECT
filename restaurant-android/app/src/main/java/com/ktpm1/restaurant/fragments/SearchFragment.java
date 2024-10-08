package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.FoodAdapter;
import com.ktpm1.restaurant.apis.CategoryApi;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Category;
import com.ktpm1.restaurant.models.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private EditText searchEditText;
    private ChipGroup chipGroupCategories;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewSearchResults);
        searchEditText = view.findViewById(R.id.searchEditText);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
        foodList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

        searchFoods("");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 0) {
                    searchFoods(s.toString());
                } else {
                    foodList.clear();
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void searchFoods(String keyword) {
        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);
        Call<List<Food>> call = foodApi.getAllFoods(keyword);
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

    private void fetchCategories() {
        CategoryApi categoryApi = ApiClient.getClient().create(CategoryApi.class);
        Call<List<Category>> call = categoryApi.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    for (Category category : categories) {
                        Chip chip = new Chip(getContext());
                        chip.setText(category.getName());
                        chip.setCheckable(true);

                        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                searchEditText.setText(category.getName());
                            }
                        });

                        chipGroupCategories.addView(chip);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}