package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Ánh xạ các view
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        tvEmptyCart = view.findViewById(R.id.tv_empty_cart);
        btnClear = view.findViewById(R.id.btn_clear);
        tvAddMoreItems = view.findViewById(R.id.tv_addMoreItem);
        toolbar = view.findViewById(R.id.toolbar);

        // Thiết lập Toolbar làm ActionBar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Icon back tùy chỉnh
                activity.getSupportActionBar().setTitle("Thông tin giỏ hàng");
            }
        }

        // Cho phép Fragment lắng nghe sự kiện MenuItem
        setHasOptionsMenu(true);

        // Xử lý sự kiện nút back trong Fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldInterceptBackPress()) {
                    Toast.makeText(getContext(), "Back pressed in CartFragment", Toast.LENGTH_SHORT).show();
                } else {
                    setEnabled(false); // Cho phép hệ thống xử lý sự kiện back
                    requireActivity().onBackPressed();
                }
            }
        });

        // Cài đặt RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, getContext());
        recyclerViewCart.setAdapter(cartAdapter);

        getCart();

        // Thêm tính năng vuốt để xóa item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);

        // Xử lý sự kiện nút Clear Cart
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

        // Xử lý sự kiện thêm món mới
        tvAddMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SearchFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    // Xử lý sự kiện MenuItem trong Toolbar (nút back)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quay lại Fragment hoặc Activity trước đó
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức kiểm tra điều kiện xử lý nút back
    private boolean shouldInterceptBackPress() {
        return false; // Trả về false nếu không cần chặn back
    }

    // ItemTouchHelper để xử lý vuốt xóa item trong giỏ hàng
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                int position = viewHolder.getAdapterPosition();

                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa món")
                        .setMessage("Bạn có chắc chắn muốn xóa món ăn này khỏi giỏ hàng không?")
                        .setPositiveButton("Xóa", (dialog, which) -> cartAdapter.removeItem(position))
                        .setNegativeButton("Hủy", (dialog, which) -> cartAdapter.notifyItemChanged(position))
                        .show();
            }
        }
    };

    // Lấy danh sách giỏ hàng từ API
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

    // Xóa toàn bộ giỏ hàng
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

    // Hiển thị hoặc ẩn thông báo khi giỏ hàng trống
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