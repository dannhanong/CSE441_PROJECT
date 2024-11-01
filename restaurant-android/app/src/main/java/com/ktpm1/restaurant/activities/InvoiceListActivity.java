package com.ktpm1.restaurant.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.InvoiceAdapter;
import com.ktpm1.restaurant.dtos.responses.InvoiceResponse;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListActivity extends AppCompatActivity {

    private RecyclerView rvInvoiceList;
    private List<InvoiceResponse> invoiceList;
    private InvoiceAdapter invoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list);

        rvInvoiceList = findViewById(R.id.rvInvoiceList);

        // Dữ liệu mẫu cho danh sách hóa đơn
        invoiceList = new ArrayList<>();
        invoiceList.add(new InvoiceResponse("1", "12345", "01/01/2024", 500000));
        invoiceList.add(new InvoiceResponse("2", "67890", "02/01/2024", 650000));
        invoiceList.add(new InvoiceResponse("3", "11223", "03/01/2024", 800000));

        invoiceAdapter = new InvoiceAdapter(this, invoiceList);
        rvInvoiceList.setLayoutManager(new LinearLayoutManager(this));
        rvInvoiceList.setAdapter(invoiceAdapter);
    }
}
