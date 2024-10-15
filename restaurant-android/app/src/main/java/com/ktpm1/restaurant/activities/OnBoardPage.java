package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

public class OnBoardPage extends AppCompatActivity {
    private Button registerButton;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_page);

        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoardPage.this, SignIn.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoardPage.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}