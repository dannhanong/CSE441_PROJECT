package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.LoginForm;
import com.ktpm1.restaurant.dtos.responses.LoginResponse;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private boolean isShowPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        ImageView isToogleButton = findViewById(R.id.ivTogglePasswordVisibility);
        isToogleButton.setOnClickListener(this::togglePasswordVisibility);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            LoginForm loginForm = new LoginForm(username, password);
            login(loginForm);
        });

        tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
    private void togglePasswordVisibility(View view){
            if(isShowPassword){
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
            }else{
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
            }
            etPassword.setSelection(etPassword.getText().length());
            isShowPassword = !isShowPassword;
    }
    private void login(LoginForm loginForm) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<LoginResponse> call = authApi.login(loginForm);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getAccessToken();

                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<User> call = authApi.getProfile("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userInfo = response.body();
                    String fullName = userInfo.getName();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("fullName", fullName);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}