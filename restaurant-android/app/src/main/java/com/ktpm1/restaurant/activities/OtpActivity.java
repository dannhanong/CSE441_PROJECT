package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

public class OtpActivity extends AppCompatActivity {
    private TextView textViewCountdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 180000;
    private TextView textviewStartCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_otp);

        EditText otpDigit1 = findViewById(R.id.otp_digit_1);
        EditText otpDigit2 = findViewById(R.id.otp_digit_2);
        EditText otpDigit3 = findViewById(R.id.otp_digit_3);
        EditText otpDigit4 = findViewById(R.id.otp_digit_4);
        EditText otpDigit5 = findViewById(R.id.otp_digit_5);
        EditText otpDigit6 = findViewById(R.id.otp_digit_6);
        textViewCountdown = findViewById(R.id.txt_countdown);
        textviewStartCountdown = findViewById(R.id.txt_Message_Otp_2);
        startCountdown();

        textviewStartCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCountdown();
            }
        });

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
    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                textViewCountdown.setText("0s");
            }
        }.start();
    }
    private void resetCountdown() {
        // Dừng timer nếu nó đang chạy
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 180000; // Đặt lại thời gian về 60 giây
        updateCountdownText(); // Cập nhật lại TextView
        startCountdown(); // Bắt đầu lại đếm ngược
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("Mã xác thực có tác dụng trong %02d:%02d", minutes, seconds);
        textViewCountdown.setText(timeLeftFormatted);
    }
}
