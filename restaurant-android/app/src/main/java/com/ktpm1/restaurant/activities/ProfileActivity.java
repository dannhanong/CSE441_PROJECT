package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ktpm1.restaurant.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView profileImage = findViewById(R.id.profileImage);
        TextView username = findViewById(R.id.username);
        TextView email = findViewById(R.id.email);
        ImageButton editProfileButton = findViewById(R.id.editProfileButton);
        ListView settingsList = findViewById(R.id.settings_list);
        Button logoutButton = findViewById(R.id.logout_button);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Chỉnh sửa profile", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, settingsOptions);
        settingsList.setAdapter(adapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(ProfileActivity.this, "Phương thức thanh toán", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(ProfileActivity.this, "Giỏ hàng của tôi", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(ProfileActivity.this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(ProfileActivity.this, "Trợ giúp & Báo cáo", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(ProfileActivity.this, "Thông báo", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(ProfileActivity.this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(ProfileActivity.this, "Tin tức & Dịch vụ", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}