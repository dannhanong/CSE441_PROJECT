package com.ktpm1.restaurant.activities;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

public class SignIn extends AppCompatActivity {

    private EditText fullName, username, email, phone, password, confirmPassword;
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
        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);

        // Xử lý sự kiện nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Toast.makeText(SignIn.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    // Xử lý sau khi đăng ký thành công
                }
            }
        });
    }

    private boolean validateInputs() {
        if (fullName.getText().toString().isEmpty()) {
            fullName.setError("Họ và tên không được để trống");
            return false;
        }
        if (username.getText().toString().isEmpty()) {
            username.setError("Tài khoản không được để trống");
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Email không được để trống");
            return false;
        }
        if (phone.getText().toString().isEmpty()) {
            phone.setError("Số điện thoại không được để trống");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Mật khẩu không được để trống");
            return false;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }
}
