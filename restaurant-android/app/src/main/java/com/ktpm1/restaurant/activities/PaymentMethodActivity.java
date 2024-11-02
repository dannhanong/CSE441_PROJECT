package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

public class PaymentMethodActivity extends AppCompatActivity {

    private RadioGroup radioGroupPayment;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupPayment.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedPaymentMethod = selectedRadioButton.getText().toString();
                    Toast.makeText(PaymentMethodActivity.this,
                            "Phương thức thanh toán: " + selectedPaymentMethod,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentMethodActivity.this,
                            "Vui lòng chọn phương thức thanh toán",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
