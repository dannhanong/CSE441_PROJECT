package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    TextView editProfileText;
    ImageView profileImage;
    TextView name, email;
    ImageButton editProfileButton;
    ListView settingsList;
    Button logoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        editProfileButton.setOnClickListener(v -> {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editProfileFragment) // Change 'fragment_container' to your actual container ID
                    .addToBackStack(null) // Add to back stack
                    .commit();
        });
        editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editProfileFragment) // Change 'fragment_container' to your actual container ID
                        .addToBackStack(null) // Add to back stack
                        .commit();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Đăng xuất", Toast.LENGTH_SHORT).show();
            }
        });

        String[] settingsOptions = {
                "Phương thức thanh toán",
                "Giỏ hàng của tôi",
                "Đổi mật khẩu",
                "Trợ giúp & Báo cáo",
                "Thông báo",
                "Chính sách bảo mật",
                "Tin tức & Dịch vụ"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, settingsOptions);
        settingsList.setAdapter(adapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "Phương thức thanh toán", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "Giỏ hàng của tôi", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, changePasswordFragment) // Change 'fragment_container' to your actual container ID
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "Trợ giúp & Báo cáo", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "Thông báo", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "Chính sách bảo mật", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "Tin tức & Dịch vụ", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        getUserInfo();

        return view;
    }

    private void init(View view) {
        editProfileText = view.findViewById(R.id.txt_edit);
        profileImage = view.findViewById(R.id.profileImage);
        name = view.findViewById(R.id.tv_name_profile);
        email = view.findViewById(R.id.tv_email_profile);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        settingsList = view.findViewById(R.id.settings_list);
        logoutButton = view.findViewById(R.id.logout_button);
    }

    private void getUserInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<User> call = authApi.getProfile("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userInfo = response.body();
                    name.setText(userInfo.getName());
                    email.setText(userInfo.getPhoneNumber());
                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
            }
        });
    }
}