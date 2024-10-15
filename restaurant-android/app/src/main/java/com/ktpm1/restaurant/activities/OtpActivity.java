package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;

import retrofit2.Call;

public class OtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        EditText otpDigit1 = findViewById(R.id.otp_digit_1);
        EditText otpDigit2 = findViewById(R.id.otp_digit_2);
        EditText otpDigit3 = findViewById(R.id.otp_digit_3);
        EditText otpDigit4 = findViewById(R.id.otp_digit_4);
        EditText otpDigit5 = findViewById(R.id.otp_digit_5);
        EditText otpDigit6 = findViewById(R.id.otp_digit_6);

        setupOtpInputs(otpDigit1, otpDigit2, otpDigit3, otpDigit4, otpDigit5, otpDigit6);
    }

    private void setupOtpInputs(EditText... otpDigits) {
        for (int i = 0; i < otpDigits.length; i++) {
            final int index = i;

            otpDigits[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpDigits.length - 1) {
                        otpDigits[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpDigits[index - 1].requestFocus();
                    } else if (index == otpDigits.length - 1) {
                        StringBuilder otp = new StringBuilder();
                        for (EditText otpDigit : otpDigits) {
                            otp.append(otpDigit.getText().toString());
                        }
                        verifyOtp(otp.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void verifyOtp(String otp) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        Call<ResponseMessage> call = authApi.verify(otp);
        call.enqueue(new retrofit2.Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage != null) {
                        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                // Handle error
            }
        });
    }
}
