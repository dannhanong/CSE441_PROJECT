package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ktpm1.restaurant.R;

public class ThankYouFragment extends Fragment {
    public ThankYouFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khi nhấn vào nút OK, đóng modal và quay lại màn hình trước đó
        view.findViewById(R.id.button_ok).setOnClickListener(v -> {
//            requireActivity().getSupportFragmentManager().popBackStack();
//            getActivity().getSupportFragmentManager().beginTransaction()
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SearchFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}