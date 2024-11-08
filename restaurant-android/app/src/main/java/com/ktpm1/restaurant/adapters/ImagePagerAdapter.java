package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;

import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {

    private List<String> fileCodes;

    public ImagePagerAdapter(List<String> fileCodes) {
        this.fileCodes = fileCodes;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String fileCode = fileCodes.get(position);

        String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;

        // Sử dụng Glide hoặc Picasso để tải ảnh
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return fileCodes.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
