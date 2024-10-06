package com.ktpm1.restaurant.fragments.homeofs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ktpm1.restaurant.R;

public class SliderPageFragment extends Fragment {

    private static final String ARG_IMAGE_RES_ID = "imageResId";
    private int imageResId;

    // Khởi tạo SliderPageFragment với hình ảnh
    public static SliderPageFragment newInstance(int imageResId) {
        SliderPageFragment fragment = new SliderPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageResId = getArguments().getInt(ARG_IMAGE_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_slider_page, container, false);

        // Hiển thị hình ảnh trong ImageView
        ImageView imageView = view.findViewById(R.id.imageViewSliderPage);
        imageView.setImageResource(imageResId);

        return view;
    }
}