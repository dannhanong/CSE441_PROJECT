package com.ktpm1.restaurant.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceListFragment extends Fragment {

    private RecyclerView rvInvoiceList;
    private List<Order> invoiceList;
    private InvoiceAdapter invoiceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invoice_list, container, false);

        rvInvoiceList = view.findViewById(R.id.rvInvoiceList);

        // Dữ liệu mẫu cho danh sách hóa đơn
        invoiceList = new ArrayList<>();
//        invoiceList.add(new InvoiceResponse("1", "12345", "01/01/2024", 500000));
//        invoiceList.add(new InvoiceResponse("2", "67890", "02/01/2024", 650000));
//        invoiceList.add(new InvoiceResponse("3", "11223", "03/01/2024", 800000));

//        invoiceAdapter = new InvoiceAdapter(this, invoiceList);
        rvInvoiceList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInvoiceList.setAdapter(invoiceAdapter);

        return view;
    }

        private void fetchReservedTables(String token) {
        OrderApi orderApi = ApiClient.getClient().create(OrderApi.class);
        Call<List<Order>> call = orderApi.getReservedTables(token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    invoiceList = response.body();
                    invoiceAdapter = new InvoiceAdapter(getContext(), invoiceList);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("ReviewOrderActivity", "Error: " + t.getMessage());
            }
        });
    }
}
