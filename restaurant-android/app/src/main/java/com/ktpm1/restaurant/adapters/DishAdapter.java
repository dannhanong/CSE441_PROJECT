package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ktpm1.restaurant.R;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private List<String> dishList; // Điều chỉnh kiểu dữ liệu nếu cần, hiện tại giả định là danh sách String

    public DishAdapter(List<String> dishList) {
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        String dish = dishList.get(position);

        // Thiết lập tên món ăn
        holder.tvProductName.setText(dish);

        // Giả lập thiết lập giá và số lượng
        holder.tvProductPrice.setText("30.000đ");
        holder.tvQuantity.setText("1");

    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvQuantity;
        ImageView imgProduct;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            // Khởi tạo các view theo đúng ID trong item_dish.xml
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            imgProduct = itemView.findViewById(R.id.img_product);
        }
    }
}
