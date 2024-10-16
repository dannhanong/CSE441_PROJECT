package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        TextView editProfileText = view.findViewById(R.id.txt_edit);
        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        ImageButton editProfileButton = view.findViewById(R.id.editProfileButton);
        ListView settingsList = view.findViewById(R.id.settings_list);
        Button logoutButton = view.findViewById(R.id.logout_button);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editProfileFragment) // Change 'fragment_container' to your actual container ID
                        .addToBackStack(null) // Add to back stack
                        .commit();
            }
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

        return view;
    }
}