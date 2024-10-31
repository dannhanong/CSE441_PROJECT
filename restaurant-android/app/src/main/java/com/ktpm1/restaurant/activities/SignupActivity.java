

package com.ktpm1.restaurant.activities;



import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText fullNameInput, usernameInput, emailInput, phoneInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private boolean isShowPassword = false;
    private boolean isShowConfirmPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần trong giao diện
        fullNameInput = findViewById(R.id.full_name);
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        phoneInput = findViewById(R.id.phone);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        ImageView passwordToggle = findViewById(R.id.ivTogglePasswordVisibility);
        ImageView confirmPasswordToggle = findViewById(R.id.ivTogglePasswordVisibility2);

        passwordToggle.setOnClickListener(this::togglePasswordVisibility);
        confirmPasswordToggle.setOnClickListener(this::toggleConfirmPasswordVisibility);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý sự kiện nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    register();
                }
            }
        });
    }
    private void togglePasswordVisibility(View view) {
        if (isShowPassword) {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
        } else {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
        }
        passwordInput.setSelection(passwordInput.getText().length());
        isShowPassword = !isShowPassword;
    }
    private void toggleConfirmPasswordVisibility(View view) {
        if (isShowConfirmPassword) {
            confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
        } else {
            confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ((ImageView) view).setImageResource(R.drawable.ic_eye_icon);
        }
        confirmPasswordInput.setSelection(confirmPasswordInput.getText().length());
        isShowConfirmPassword = !isShowConfirmPassword;
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

    private void register() {

        String fullName = fullNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phoneNumber = phoneInput.getText().toString();
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (password.equals(confirmPassword)) {
            RegisterRequest registerRequest = new RegisterRequest(fullName, username, password, confirmPassword, email, phoneNumber);

            AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
            Call<User> call = authApi.signup(registerRequest);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();

                        sendVerificationCode(username, phoneNumber);

                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    Toast.makeText(SignupActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode(String username, String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                String code = phoneAuthCredential.getSmsCode();
//                                if (code != null) {
//                                    sendOtpToServer(code);
//                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                sendOtpToServer(username, s);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void sendOtpToServer(String username, String otp) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        Call<ResponseMessage> call = authApi.updateVerifyCode(username, otp);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    ResponseMessage responseMessage = response.body();
                    Toast.makeText(SignupActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp(String otp) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        Call<ResponseMessage> call = authApi.verify(otp);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    ResponseMessage responseMessage = response.body();
                    Toast.makeText(SignupActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
