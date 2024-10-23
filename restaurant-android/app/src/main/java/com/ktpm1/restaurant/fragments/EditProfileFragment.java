package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    private EditText nameEditText;
    private EditText emailEditText;
    private Button backButton;
    private Button saveButton;
    private ImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize views
        nameEditText = view.findViewById(R.id.txt_nameEditText);
        emailEditText = view.findViewById(R.id.txt_emailEditText);
        backButton = view.findViewById(R.id.btn_back);
        saveButton = view.findViewById(R.id.btn_save);
        profileImage = view.findViewById(R.id.img_profile);

        // Load user information
        loadUserInfo();

        // Set click listeners
        backButton.setOnClickListener(v -> {
            // Handle back button click (navigate back)
            requireActivity().onBackPressed();
        });

        saveButton.setOnClickListener(v -> {
            // Handle save button click (update profile)
            saveUserInfo();
        });

        return view;
    }

    private void loadUserInfo() {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<User> call = authApi.getProfile("Bearer " + token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        String fileCode = user.getAvatarCode();
                        String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;
                        nameEditText.setText(user.getName());
                        emailEditText.setText(user.getEmail());
                        Glide.with(requireContext()).load(imageUrl).into(profileImage);
                    }
                } else {
                    Log.e("EditProfileFragment", "Failed to load user information");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
                Log.e("EditProfileFragment", "Failed to load user information", t);
            }
        });
    }

    private void saveUserInfo() {
        String updatedName = nameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();

        Log.d("EditProfileFragment", "Updated Name: " + updatedName);
        Log.d("EditProfileFragment", "Updated Email: " + updatedEmail);

        // Optionally, show a success message or navigate back
        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}

