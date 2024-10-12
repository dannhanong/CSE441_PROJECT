package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.CartItem;
import com.ktpm1.restaurant.models.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        Food food = item.getFood();
        String fileCode = food.getImageCode();
        String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;

        holder.productName.setText(item.getFood().getName());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(holder.itemView).load(imageUrl).into(holder.productImage);

        // Tính toán giá dựa trên số lượng
        int totalPrice = (int) item.getPrice();
        holder.productPrice.setText(totalPrice + "đ");

        // Xử lý sự kiện tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            int updatedPrice = (int) item.getPrice() * item.getQuantity();
            holder.productPrice.setText(updatedPrice + "đ");
        });

        // Xử lý sự kiện giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                int updatedPrice = (int) item.getPrice() * item.getQuantity();
                holder.productPrice.setText(updatedPrice + "đ");
            }
        });
    }

    public void setCartItems(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged(); // Cập nhật lại danh sách hiển thị
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity;
        ImageButton btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}
