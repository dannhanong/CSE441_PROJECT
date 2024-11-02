package com.ktpm1.restaurant.fragments.homeofs;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.SliderAdapter;
import com.ktpm1.restaurant.apis.FoodApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SliderFragment extends Fragment {

    private ViewPager2 viewPager;
    private SliderAdapter sliderAdapter;
    private List<Integer> imageList;
    private Handler sliderHandler;
    private Runnable sliderRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        imageList = Arrays.asList(
                R.drawable.km1,
                R.drawable.km2,
                R.drawable.km3
        );

        viewPager = view.findViewById(R.id.viewPager);
        sliderAdapter = new SliderAdapter(requireActivity(), imageList);
        viewPager.setAdapter(sliderAdapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(new MarginPageTransformer(40));

        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        recyclerView.setPadding(80, 0, 80, 0);
        recyclerView.setClipToPadding(false);

        CircleIndicator3 tabLayout = view.findViewById(R.id.tabLayout);
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//        }).attach();

        tabLayout.setViewPager(viewPager);
        sliderAdapter.registerAdapterDataObserver(tabLayout.getAdapterDataObserver());
        tabLayout.createIndicators(imageList.size(), 0);

        autoSlide();

        return view;
    }

    private void autoSlide() {
        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = sliderAdapter.getItemCount();

                if (currentItem < totalItems - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }

                sliderHandler.postDelayed(this, 6000);
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 6000);
    }
//    public void getSliderImages() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", null);
//
//        FoodApi foodApi = ApiClient.getClient().create(FoodApi.class);
//        Call<List<Food>> call = foodApi.getTop5Foods("Bearer " + token);
//        call.enqueue(new Callback<List<Food>>() {
//            @Override
//            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
//                if (response.isSuccessful()) {
//                    List<Food> foodList = response.body();
//                    for (Food food : foodList) {
//                        imageList = food.getImageList();
//                    }
//                    sliderAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Food>> call, Throwable throwable) {
//
//            }
//        });
//    }
}