package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.Food; // Sử dụng class Food
import java.util.List;

public class FoodHistoryAdapter extends RecyclerView.Adapter<FoodHistoryAdapter.FoodHistoryViewHolder> {

    private List<Food> foodList; // Thay đổi thành List<Food>

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
        Food food = foodList.get(position); // Thay đổi thành Food

        holder.tvFoodName.setText(food.getName());
        // Bạn có thể sửa lại cách hiển thị các thuộc tính khác của Food
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvFoodTime, tvFoodTable, tvStatus;

        public FoodHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodTime = itemView.findViewById(R.id.tvFoodTime);
            tvFoodTable = itemView.findViewById(R.id.tvFoodTable);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
