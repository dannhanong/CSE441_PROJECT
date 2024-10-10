package com.ktpm1.restaurant.activities;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText fullNameInput, usernameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_sign_in);

        // Ánh xạ các thành phần trong giao diện
        fullNameInput = findViewById(R.id.full_name);
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);

        // Xử lý sự kiện nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private boolean validateInputs() {
        if (fullNameInput.getText().toString().isEmpty()) {
            fullNameInput.setError("Họ và tên không được để trống");
            return false;
        }
        if (usernameInput.getText().toString().isEmpty()) {
            usernameInput.setError("Tài khoản không được để trống");
            return false;
        }
        if (emailInput.getText().toString().isEmpty()) {
            emailInput.setError("Email không được để trống");
            return false;
        }
        if (passwordInput.getText().toString().isEmpty()) {
            passwordInput.setError("Mật khẩu không được để trống");
            return false;
        }
        if (!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
            confirmPasswordInput.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }

    private void Register() {

        String fullName = fullNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String userName = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (password.equals(confirmPassword)) {
            // Tạo Set cho vai trò
            Set<String> roles = new HashSet<>();
            roles.add("USER"); // Thêm vai trò mặc định là "USER"

            // Tạo đối tượng RegisterRequest với đầy đủ các tham số
            RegisterRequest registerRequest = new RegisterRequest(fullName, userName, password, confirmPassword, email, roles);

            AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
            Call<ResponseMessage> call = authApi.signup(registerRequest);

            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        ResponseMessage responseMessage = response.body();
                        Toast.makeText(SignInActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                    Toast.makeText(SignInActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
        }
    }
}


