package com.ktpm1.restaurant.fragments.homeofs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.InvoiceListFragment;
import com.ktpm1.restaurant.adapters.MenuAdapter;
import com.ktpm1.restaurant.fragments.CatalogFragment;
import com.ktpm1.restaurant.fragments.PaymentFragment;
import com.ktpm1.restaurant.models.MenuItem;

import java.util.Arrays;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMenu);

        List<MenuItem> menuItems = Arrays.asList(
                new MenuItem(R.drawable.foodicon, "Đồ ăn"),
                new MenuItem(R.drawable.tableicon, "Bàn"),
                new MenuItem(R.drawable.paymenticon, "Thanh toán")
        );

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        menuAdapter = new MenuAdapter(menuItems, position -> {
            switch (position) {
                case 0:
                    // Điều hướng đến Fragment Đồ ăn
//                    openFragment(new FoodFragment());
                    break;
                case 1:
                    openFragment(new CatalogFragment());
                    break;
                case 2:
                    // Điều hướng đến Fragment Thanh toán
                    openFragment(new InvoiceListFragment());
                    break;
            }
        });
        recyclerView.setAdapter(menuAdapter);

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}