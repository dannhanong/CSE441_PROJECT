package com.ktpm1.restaurant.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.TableAdapter;
import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.ordertable, container, false);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Grid với 3 cột

        // Tạo danh sách các bàn
        List<String> tableList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            tableList.add("Bàn " + i + "\nBàn trống");
        }

        // Gắn Adapter vào RecyclerView
        TableAdapter adapter = new TableAdapter(tableList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
