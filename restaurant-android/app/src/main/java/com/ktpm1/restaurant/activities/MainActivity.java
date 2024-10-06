package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.fragments.HomeFragment;
import com.ktpm1.restaurant.fragments.ProfileFragment;
import com.ktpm1.restaurant.fragments.RecentFragment;
import com.ktpm1.restaurant.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new HomeFragment())
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

                getSupportFragmentManager().beginTransaction().replace(R.id.main, selectedFragment).commit();
                return true;
            }
        });
    }
}