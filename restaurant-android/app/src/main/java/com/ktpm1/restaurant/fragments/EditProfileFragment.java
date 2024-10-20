package com.ktpm1.restaurant.fragments;

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

import com.ktpm1.restaurant.R;

public class EditProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private Button backButton;
    private Button saveButton;

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
        // This is where you would retrieve the user's information from your data source
        // For example, using SharedPreferences, a database, or a network call
        // For demo, let's use hardcoded values

        String userName = "Lê Văn Khải";  // Replace with actual data retrieval
        String userEmail = "example@mail.com";  // Replace with actual data retrieval

        nameEditText.setText(userName);
        emailEditText.setText(userEmail);
    }

    private void saveUserInfo() {
        // Retrieve input from EditText fields
        String updatedName = nameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();

        // Here you would save the updated information to your data source
        // For example, saving to SharedPreferences, a database, or sending a network request
        // For demo, just log the updated values
        Log.d("EditProfileFragment", "Updated Name: " + updatedName);
        Log.d("EditProfileFragment", "Updated Email: " + updatedEmail);

        // Optionally, show a success message or navigate back
        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}

