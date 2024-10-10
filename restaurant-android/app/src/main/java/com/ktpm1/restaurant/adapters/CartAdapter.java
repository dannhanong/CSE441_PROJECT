package com.ktpm1.restaurant.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.CartItem;

import java.util.List;

import androidx.appcompat.widget.AppCompatImageButton;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private OnCartUpdateListener cartUpdateListener;

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartUpdateListener cartUpdateListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.cartUpdateListener = cartUpdateListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.productName.setText(item.getProductName());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Tính toán giá dựa trên số lượng
        int totalPrice = item.getBasePrice() * item.getQuantity();
        holder.productPrice.setText(totalPrice + "đ");

        // Xử lý sự kiện tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            int updatedPrice = item.getBasePrice() * item.getQuantity();
            holder.productPrice.setText(updatedPrice + "đ");

            notifyItemChanged(position);
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });

        // Xử lý sự kiện giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                int updatedPrice = item.getBasePrice() * item.getQuantity();
                holder.productPrice.setText(updatedPrice + "đ");

                notifyItemChanged(position);
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (cartItems != null) ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity;
        AppCompatImageButton btnIncrease, btnDecrease; // Thay đổi từ Button sang AppCompatImageButton


        @SuppressLint("WrongViewCast")
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase); // Đảm bảo rằng nút này là AppCompatImageButton trong XML
            btnDecrease = itemView.findViewById(R.id.btn_decrease); // Đảm bảo rằng nút này là AppCompatImageButton trong XML

        }
    }

    // Interface để cập nhật giỏ hàng từ CartActivity
    public interface OnCartUpdateListener {
        void onCartUpdated();
    }
}
