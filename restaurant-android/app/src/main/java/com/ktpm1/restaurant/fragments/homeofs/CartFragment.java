package com.ktpm1.restaurant.fragments.homeofs;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.CartAdapter;
import com.ktpm1.restaurant.apis.CartApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.CartItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;  // Sử dụng List thay vì Set

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItems = new ArrayList<>();

        cartAdapter = new CartAdapter(cartItems);
        recyclerViewCart.setAdapter(cartAdapter);

        getCart();

        return view;
    }

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
                    Set<CartItem> cartItemSet = cart.getCartItems();  // API trả về Set<CartItem>
                    List<CartItem> cartItemList = new ArrayList<>(cartItemSet);

                    Toast.makeText(getContext(), "Cart size: " + cartItemList.size(), Toast.LENGTH_SHORT).show();

                    cartItems.clear();
                    cartItems.addAll(cartItemList);  // Cập nhật List với các phần tử từ API
                    cartAdapter.setCartItems(cartItems);  // Cập nhật adapter
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}