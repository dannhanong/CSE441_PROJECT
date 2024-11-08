package com.ktpm1.restaurant.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    private EditText nameEditText, emailEditText;
    private Button backButton, saveButton;
    private ImageView profileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private File avatarFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }

        nameEditText = view.findViewById(R.id.txt_nameEditText);
        emailEditText = view.findViewById(R.id.txt_emailEditText);
        backButton = view.findViewById(R.id.btn_back);
        saveButton = view.findViewById(R.id.btn_save);
        profileImage = view.findViewById(R.id.img_profile);

        loadUserInfo();

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        saveButton.setOnClickListener(v -> updateProfile());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                avatarFile = createTempFileFromStream(inputStream, "avatar.jpg");
                Log.d("EditProfileFragment", "Avatar File Path: " + avatarFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createTempFileFromStream(InputStream inputStream, String fileName) throws IOException {
        File tempFile = new File(requireContext().getCacheDir(), fileName);
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
        return tempFile;
    }

    private void loadUserInfo() {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
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
                        Glide.with(requireContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(profileImage);
                    }
                } else {
                    Log.e("EditProfileFragment", "Failed to load user information");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("EditProfileFragment", "Failed to load user information", t);
            }
        });
    }

    private void updateProfile() {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null || token.isEmpty()) {
            Log.e("EditProfileFragment", "Token is null or empty");
            return;
        }

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        RequestBody updatedName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody updatedEmail = RequestBody.create(MediaType.parse("text/plain"), email);

        MultipartBody.Part avatarPart = null;
        if (avatarFile != null && avatarFile.exists()) {
            avatarPart = MultipartBody.Part.createFormData(
                    "avatar",
                    avatarFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), avatarFile)
            );
        } else {
            Log.e("EditProfileFragment", "Avatar file is either null or doesn't exist.");
        }

        Call<ResponseMessage> call = authApi.updateProfile("Bearer " + token, updatedName, updatedEmail, avatarPart);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.e("EditProfileFragment", "Response error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e("EditProfileFragment", "Failed to update user information", t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Storage permission is required to select avatar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}