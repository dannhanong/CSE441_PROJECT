package com.ktpm1.restaurant.activities;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText fullNameInput, usernameInput, emailInput, phoneInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private String otp = "", phoneNumberToVerify="";

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
        phoneInput = findViewById(R.id.phone);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);

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
                        phoneNumberToVerify = phoneNumber;
                        otp = user.getVerificationCode();
                        if (ContextCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            sendOtp();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOtp();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Quyền gửi SMS đã bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendOtp() {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(otp + " là mã xác thực của bạn");
        String phoneNumberWithCountryCode = phoneNumberToVerify.startsWith("0")
                ? "+84" + phoneNumberToVerify.substring(1)
                : "+84" + phoneNumberToVerify;
        smsManager.sendMultipartTextMessage(phoneNumberWithCountryCode, null, parts, null, null);
    }

//    private void sendVerificationCode(String username, String phoneNumber) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////                                String code = phoneAuthCredential.getSmsCode();
////                                if (code != null) {
////                                    sendOtpToServer(code);
////                                }
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(s, forceResendingToken);
//                                sendOtpToServer(username, s);
//                            }
//                        })
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private void sendOtpToServer(String username, String otp) {
//        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
//        Call<ResponseMessage> call = authApi.updateVerifyCode(username, otp);
//        call.enqueue(new Callback<ResponseMessage>() {
//            @Override
//            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
//                if (response.isSuccessful()) {
//                    ResponseMessage responseMessage = response.body();
//                    Toast.makeText(SignupActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
//                Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void verifyOtp(String otp) {
//        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
//        Call<ResponseMessage> call = authApi.verify(otp);
//        call.enqueue(new Callback<ResponseMessage>() {
//            @Override
//            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
//                if (response.isSuccessful()) {
//                    ResponseMessage responseMessage = response.body();
//                    Toast.makeText(SignupActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
//                Toast.makeText(SignupActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}


