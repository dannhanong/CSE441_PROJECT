package com.ktpm1.restaurant.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.fragments.homeofs.HeaderFragment;
import com.ktpm1.restaurant.fragments.homeofs.MenuFragment;
import com.ktpm1.restaurant.fragments.homeofs.SliderFragment;
import com.ktpm1.restaurant.fragments.homeofs.SuggestionsFragment;

import java.time.LocalTime;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    TextView textViewSuggestion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.headerContainer, new HeaderFragment())
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

        textViewSuggestion = view.findViewById(R.id.tvSuggestion);
        textViewSuggestion.setText(getSuggestion());

        return view;
    }

    private String getSuggestion() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if (hour >= 6 && hour < 10) {
            return "Gợi ý cho buổi sáng";
        } else if (hour >= 10 && hour < 14) {
            return "Gợi ý cho buổi trưa";
        } else if (hour >= 14 && hour < 18) {
            return "Gợi ý cho buổi chiều";
        } else if (hour >= 18 && hour < 22) {
            return "Gợi ý cho buổi tối";
        }
        return "Tạm thời chưa có gợi ý";
    }
}