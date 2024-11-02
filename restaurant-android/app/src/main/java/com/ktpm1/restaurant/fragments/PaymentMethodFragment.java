package com.ktpm1.restaurant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ktpm1.restaurant.R;

public class PaymentMethodFragment extends Fragment {
    private RadioGroup radioGroupPayment;
    private Button btnPay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_payment_method, container, false);

        radioGroupPayment = view.findViewById(R.id.radioGroupPayment);
        btnPay = view.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupPayment.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = view.findViewById(selectedId);
                    String selectedPaymentMethod = selectedRadioButton.getText().toString();
                    Toast.makeText(getActivity(),
                            "Phương thức thanh toán: " + selectedPaymentMethod,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            "Vui lòng chọn phương thức thanh toán",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
