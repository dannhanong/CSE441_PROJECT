package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.fragments.FoodDetailFragment;
import com.ktpm1.restaurant.fragments.HomeFragment;
import com.ktpm1.restaurant.fragments.ProfileFragment;
import com.ktpm1.restaurant.fragments.RecentFragment;
import com.ktpm1.restaurant.fragments.SearchFragment;
import com.ktpm1.restaurant.fragments.homeofs.SuggestionsFragment;

public class MainActivity extends AppCompatActivity implements SuggestionsFragment.OnFoodSelectedListener{
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        String fullName = intent.getStringExtra("fullName");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
//            HomeFragment homeFragment = new HomeFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("fullName", fullName);
//            homeFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navigation_search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.navigation_recent) {
                    selectedFragment = new RecentFragment();
                } else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onFoodSelected(Long foodId) {
        // Thay thế bằng FoodDetailFragment khi món ăn được chọn
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FoodDetailFragment(foodId));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}