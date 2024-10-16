package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<String> tableList;

    public TableAdapter(List<String> tableList) {
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        String tableInfo = tableList.get(position);
        holder.tableButton.setText(tableInfo);
        holder.tableButton.setOnClickListener(v -> {
            // Xử lý sự kiện khi nhấn vào bàn
            // Ví dụ hiển thị Toast
        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        Button tableButton;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableButton = itemView.findViewById(R.id.table_button);
        }
    }
}
