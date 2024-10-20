package com.ktpm1.restaurant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ktpm1.restaurant.R;

public class ChangePasswordFragment extends Fragment {

    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonChangePassword;
    private TextView textViewMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_changepassword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextCurrentPassword = view.findViewById(R.id.txt_OldPassword);
        editTextNewPassword = view.findViewById(R.id.txt_NewPassword);
        editTextConfirmPassword = view.findViewById(R.id.txt_ConfirmPassword);
        buttonChangePassword = view.findViewById(R.id.btn_Save);
        textViewMessage = view.findViewById(R.id.textViewMessage);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!newPassword.equals(confirmPassword)) {
            textViewMessage.setText("Mật khẩu nhập lại không hợp lệ.");
            return;
        }

        // TODO: Call your backend service to change the password
        // For example:
        // if (backendService.changePassword(currentPassword, newPassword)) {
        //     textViewMessage.setText("Password changed successfully!");
        // } else {
        //     textViewMessage.setText("Error changing password. Please try again.");
        // }

        // Clear fields after attempt (just for demonstration)
        editTextCurrentPassword.setText("");
        editTextNewPassword.setText("");
        editTextConfirmPassword.setText("");
    }
}

