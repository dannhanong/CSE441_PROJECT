package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.ChangePasswordRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonChangePassword;
    private TextView textViewMessage;
//    private Button buttonBack;
    private Toolbar toolbar;
    private Button buttonBack;
    boolean[] isOldPasswordVisible = {false};
    boolean[] isNewPasswordVisible = {false};
    boolean[] isConfirmPasswordVisible = {false};

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
        toolbar = view.findViewById(R.id.toolbar);
//        buttonBack = view.findViewById(R.id.btn_Back);

        // Thiết lập Toolbar làm ActionBar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Icon back tùy chỉnh
                activity.getSupportActionBar().setTitle("Đổi mật khẩu");
            }
        }

        setHasOptionsMenu(true);

        // Xử lý sự kiện nút back trong Fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldInterceptBackPress()) {
                    Toast.makeText(getContext(), "Back pressed in CartFragment", Toast.LENGTH_SHORT).show();
                } else {
                    setEnabled(false); // Cho phép hệ thống xử lý sự kiện back
                    requireActivity().onBackPressed();
                }
            }
        });
        buttonBack = view.findViewById(R.id.btn_Back);
        editTextCurrentPassword = view.findViewById(R.id.txt_OldPassword);
        editTextNewPassword = view.findViewById(R.id.txt_NewPassword);
        editTextConfirmPassword = view.findViewById(R.id.txt_ConfirmPassword);


        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        buttonBack.setOnClickListener(view1 -> {
            ProfileFragment profileFragment = new ProfileFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, profileFragment) // Change 'fragment_container' to your actual container ID
                    .addToBackStack(null)
                    .commit();
        });
        setPasswordVisibilityToggle(editTextCurrentPassword, isOldPasswordVisible);
        setPasswordVisibilityToggle(editTextNewPassword, isNewPasswordVisible);
        setPasswordVisibilityToggle(editTextConfirmPassword, isConfirmPasswordVisible);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPasswordVisibilityToggle(EditText editText, boolean[] isVisible) {
        editText.setOnTouchListener((View v, MotionEvent event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {

                    if (isVisible[0]) {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed_icon, 0); // icon mắt
                    } else {
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_icon, 0); // icon đóng mắt
                    }
                    isVisible[0] = !isVisible[0];
                    editText.setSelection(editText.getText().length());
                    return true;
                }
            }
            return false;
        });
//        buttonBack.setOnClickListener(view1 -> {
//            ProfileFragment profileFragment = new ProfileFragment();
//            requireActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, profileFragment) // Change 'fragment_container' to your actual container ID
//                    .addToBackStack(null)
//                    .commit();
//        });
    }

    // Xử lý sự kiện MenuItem trong Toolbar (nút back)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quay lại Fragment hoặc Activity trước đó
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức kiểm tra điều kiện xử lý nút back
    private boolean shouldInterceptBackPress() {
        return false; // Trả về false nếu không cần chặn back
    }

    private void changePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            textViewMessage.setText("Các ô không được để trống.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            textViewMessage.setText("Mật khẩu nhập lại không trùng, vui lòng nhập lại.");
            return;
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        Call<ResponseMessage> call = authApi.changePassword("Bearer " + token,
                new ChangePasswordRequest(currentPassword, newPassword, confirmPassword));
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    // Password successfully updated
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage.getStatus() == 200) {
                        textViewMessage.setText("Đổi mật khẩu thành công.");
                        clearFields();
                    } else {
                        textViewMessage.setText(responseMessage.getMessage());
                    }

//                    buttonBack.setOnClickListener(view -> {
//                        ProfileFragment profileFragment = new ProfileFragment();
//                        requireActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container, profileFragment)
//                                .addToBackStack(null)
//                                .commit();
//                    });
                } else {
                    textViewMessage.setText("Không đổi được mật khẩu.");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                textViewMessage.setText("Error: " + throwable.getMessage());
            }
        });
    }

    private void clearFields() {
        editTextCurrentPassword.setText("");
        editTextNewPassword.setText("");
        editTextConfirmPassword.setText("");
    }


}

