package com.ktpm1.restaurant.fragments.homeofs;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeaderFragment extends Fragment {
    private TextView textViewUserName;

    public HeaderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        Bundle bundle = getArguments();
//        String fullName = bundle != null ? bundle.getString("fullName") : "User";

        textViewUserName = view.findViewById(R.id.textViewUserName);
        getUserInfo();
        return view;
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
                    String fullName = userInfo.getName();
                    textViewUserName.setText("Xin chào, " + fullName);
                } else {
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
            }
        });
    }
}