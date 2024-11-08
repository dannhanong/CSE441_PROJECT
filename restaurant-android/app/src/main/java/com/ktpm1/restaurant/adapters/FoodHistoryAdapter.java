package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.Food;
import java.util.List;

public class FoodHistoryAdapter extends RecyclerView.Adapter<FoodHistoryAdapter.FoodHistoryViewHolder> {
    private List<Food> foodList;

    public FoodHistoryAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_history, parent, false);
        return new FoodHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHistoryViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.tvFoodName.setText(food.getName());
        String fileCode = food.getImageCode();
        String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;
        holder.tvFoodPrice.setText(String.valueOf(food.getPrice()) + " VNĐ");
        Glide.with(holder.itemView).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvFoodPrice, tvStatus;

        public FoodHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.img_history_food);
            tvFoodName = itemView.findViewById(R.id.tv_history_foodName);
            tvFoodPrice = itemView.findViewById(R.id.tv_price_history_food);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
