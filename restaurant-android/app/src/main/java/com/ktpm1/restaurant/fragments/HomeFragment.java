package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.fragments.homeofs.HeaderFragment;
import com.ktpm1.restaurant.fragments.homeofs.MenuFragment;
import com.ktpm1.restaurant.fragments.homeofs.SliderFragment;
import com.ktpm1.restaurant.fragments.homeofs.SuggestionsFragment;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = getArguments();
        String fullName = bundle != null ? bundle.getString("fullName") : "User";
        HeaderFragment headerFragment = new HeaderFragment();
        Bundle headerBundle = new Bundle();
        headerBundle.putString("fullName", fullName);
        headerFragment.setArguments(headerBundle);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.headerContainer, headerFragment)
                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.sliderContainer, new SliderFragment())
                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.menuContainer, new MenuFragment())
                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.suggestionsContainer, new SuggestionsFragment())
                .commit();

        return view;
    }
}