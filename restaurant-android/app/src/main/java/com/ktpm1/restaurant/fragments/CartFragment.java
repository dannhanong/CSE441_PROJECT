package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.CartAdapter;
import com.ktpm1.restaurant.apis.CartApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private TextView tvEmptyCart, tvAddMoreItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private Button btnClear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        tvEmptyCart = view.findViewById(R.id.tv_empty_cart);
        btnClear = view.findViewById(R.id.btn_clear);
        tvAddMoreItems = view.findViewById(R.id.tv_addMoreItem);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItems = new ArrayList<>();

        cartAdapter = new CartAdapter(cartItems, getContext());
        recyclerViewCart.setAdapter(cartAdapter);

        getCart();

        // Thêm tính năng vuốt để xóa item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa xóa giỏ hàng")
                        .setMessage("Bạn có chắc chắn muốn xóa tất cả món ăn khỏi giỏ hàng?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            clearCart();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        tvAddMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đến fragment SearchFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SearchFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                int position = viewHolder.getAdapterPosition(); // Lưu lại vị trí của item

                // Hiển thị hộp thoại xác nhận
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa món")
                        .setMessage("Bạn có chắc chắn muốn xóa món ăn này khỏi giỏ hàng không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Khi chọn "Xóa", xóa item
                            cartAdapter.removeItem(position);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            // Khi chọn "Hủy", khôi phục lại trạng thái ban đầu của item
                            cartAdapter.notifyItemChanged(position);
                        })
                        .show();
            }
        }

//        @Override
//        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                // Vẽ nền hoặc nút xóa khi vuốt
//                View itemView = viewHolder.itemView;
//                Paint p = new Paint();
//                p.setColor(Color.RED);
//                RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//                c.drawRect(background, p);
//
//                // Có thể thêm biểu tượng nút xóa (icon)
//                Drawable deleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete); // Thêm icon của bạn
//                int itemHeight = itemView.getBottom() - itemView.getTop();
//                int iconMargin = (itemHeight - deleteIcon.getIntrinsicHeight()) / 2;
//                int iconTop = itemView.getTop() + iconMargin;
//                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
//                int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
//                int iconRight = itemView.getRight() - iconMargin;
//
//                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//                deleteIcon.draw(c);
//            }
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
    };

    private void getCart() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        CartApi cartApi = ApiClient.getClient().create(CartApi.class);
        Call<Cart> call = cartApi.viewCart("Bearer " + token);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    Cart cart = response.body();
                    Set<CartItem> cartItemSet = cart.getCartItems();
                    List<CartItem> cartItemList = new ArrayList<>(cartItemSet);
                    cartItems.clear();
                    cartItems.addAll(cartItemList);
                    cartAdapter.setCartItems(cartItems);
                    toggleEmptyCartMessage();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                toggleEmptyCartMessage();
            }
        });
    }

    public void clearCart() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        CartApi cartApi = ApiClient.getClient().create(CartApi.class);
        Call<ResponseMessage> call = cartApi.clearCart("Bearer " + token);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    cartItems.clear();
                    cartAdapter.setCartItems(cartItems);
                    toggleEmptyCartMessage();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {

            }
        });
    }

    private void toggleEmptyCartMessage() {
        if (cartItems.size() == 0) {
            recyclerViewCart.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
        } else {
            recyclerViewCart.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
        }
    }
}