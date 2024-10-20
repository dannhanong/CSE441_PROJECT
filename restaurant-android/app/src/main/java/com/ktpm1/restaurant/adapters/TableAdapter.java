package com.ktpm1.restaurant.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<Table> tableList;

    public TableAdapter(List<Table> tableList) {
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
        Table table = tableList.get(position);
        holder.tableButton.setText(table.getTableNumber());
        if (!table.isAvailable()) {
            // Bàn không có sẵn, thay đổi giao diện
            holder.tableButton.setAlpha(0.7f);
            holder.tableButton.setTextColor(Color.GRAY);
            holder.tvTableStatus.setTextColor(Color.GRAY);
            holder.tableButton.setEnabled(false);
        }
        holder.tvTableStatus.setText(table.isAvailable() ? "Trống" : "Đã đặt");
//        holder.tableButton.setOnClickListener(v -> {
//            if (table.isAvailable()) {}
//        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        Button tableButton;
        TextView tvTableStatus;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableButton = itemView.findViewById(R.id.btn_tableNumber);
            tvTableStatus = itemView.findViewById(R.id.tv_tableStatus);
        }
    }
}
