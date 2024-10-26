package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

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
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }
}
