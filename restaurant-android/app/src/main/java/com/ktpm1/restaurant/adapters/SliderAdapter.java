package com.ktpm1.restaurant.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ktpm1.restaurant.fragments.homeofs.SliderPageFragment;

import java.util.List;

public class SliderAdapter extends FragmentStateAdapter {

    private List<Integer> imageList;

    public SliderAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> imageList) {
        super(fragmentActivity);
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SliderPageFragment.newInstance(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
