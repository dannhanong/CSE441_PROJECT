package com.ktpm1.restaurant.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.CartApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.CartItem;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.FoodOption;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private Context context;

    public CartAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
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
        Glide.with(holder.itemView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.productImage);

        // Tính toán giá dựa trên số lượng
        int totalPrice = (int) item.getPrice();
        holder.productPrice.setText(totalPrice + "đ");

        if (item.getOptions() != null && !item.getOptions().isEmpty()) {
            holder.llOptions.setVisibility(View.VISIBLE);
            for (int i = 0; i < item.getOptions().size(); i++) {
                FoodOption option = item.getOptions().get(i);
                TextView optionView = new TextView(holder.itemView.getContext());
                if (i == item.getOptions().size() - 1) {
                    optionView.setText(option.getName());
                } else {
                    optionView.setText(option.getName() + ", ");
                }
                optionView.setTextSize(14);
                holder.llOptions.addView(optionView);
            }
        }else {
            TextView noOptionView = new TextView(holder.itemView.getContext());
            noOptionView.setText("Không có tùy chọn");
            noOptionView.setTextSize(14);
            holder.llOptions.addView(noOptionView);
        }

        // Xử lý sự kiện tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            holder.quantity.setText(String.valueOf(currentQuantity + 1));
            int updatedPrice = (int) food.getPrice() * (currentQuantity + 1) + item.getOptions().stream().mapToInt(FoodOption::getPrice).sum() * (currentQuantity + 1);
            holder.productPrice.setText(updatedPrice + "đ");
        });

        // Xử lý sự kiện giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            if (currentQuantity > 1) {
                holder.quantity.setText(String.valueOf(currentQuantity - 1));
                int updatedPrice = (int) food.getPrice() * (currentQuantity - 1) + item.getOptions().stream().mapToInt(FoodOption::getPrice).sum() * (currentQuantity - 1);
                holder.productPrice.setText(updatedPrice + "đ");
            } else {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Xác nhận xóa món")
                        .setMessage("Bạn có chắc chắn muốn xóa món ăn này khỏi giỏ hàng không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            removeItem(position);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
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
        LinearLayout llOptions;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            llOptions = itemView.findViewById(R.id.ll_options);
        }
    }

    public void removeItem(int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        CartApi cartApi = ApiClient.getClient().create(CartApi.class);
        Call<ResponseMessage> call = cartApi.removeFromCart("Bearer " + token, cartItemList.get(position).getId());
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    cartItemList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    // Xử lý khi không xóa được
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                // Xử lý khi lỗi
            }
        });
    }
}
