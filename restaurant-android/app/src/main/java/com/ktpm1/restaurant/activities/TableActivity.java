package com.ktpm1.restaurant.activities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.TableAdapter;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordertable);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Grid với 3 cột

        // Tạo danh sách các bàn
        List<String> tableList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            tableList.add("Bàn " + i + "\nBàn trống");
        }

        // Gắn Adapter vào RecyclerView
        TableAdapter adapter = new TableAdapter(tableList);
        recyclerView.setAdapter(adapter);
    }
}
