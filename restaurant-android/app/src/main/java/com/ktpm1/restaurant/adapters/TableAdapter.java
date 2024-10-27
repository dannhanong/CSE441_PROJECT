package com.ktpm1.restaurant.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.dtos.responses.TableResponse;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<TableResponse> tableList;
    private List<Long> selectedTableIds = new ArrayList<>();
    private List<String> selectedTableNumbers = new ArrayList<>();
    private OnTableSelectionChangedListener listener;

    public interface OnTableSelectionChangedListener {
        void onTableSelectionChanged(List<Long> selectedTableIds, List<String> selectedTableNumbers);
    }

    public TableAdapter(List<TableResponse> tableList) {
        this.tableList = tableList;
    }

    public TableAdapter(List<TableResponse> tableList, OnTableSelectionChangedListener listener) {
        this.tableList = tableList;
        this.listener = listener;
    }

    public List<Long> getSelectedTableIds() {
        return new ArrayList<>(selectedTableIds);
    }

    public List<String> getSelectedTableNumbers() {
        return new ArrayList<>(selectedTableNumbers);
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        TableResponse table = tableList.get(position);
        holder.tableButton.setText(table.getTableNumber());
        if (!table.isAvailable()) {
            holder.tableButton.setAlpha(0.7f);
            holder.tableButton.setTextColor(Color.GRAY);
            holder.tvTableStatus.setTextColor(Color.GRAY);
            holder.tableButton.setEnabled(false);
        }
        holder.tvTableStatus.setText(table.isAvailable() ? "Trống" : "Đã đặt");
        holder.tableButton.setOnClickListener(v -> {
            if (table.isSelected()) {
                table.setSelected(false);
                holder.tableButton.setAlpha(1f);
                holder.tableButton.setTextColor(Color.BLACK);
                holder.tvTableStatus.setTextColor(Color.BLACK);
                holder.tvTableStatus.setText(table.isAvailable() ? "Trống" : "Đã đặt");
                holder.tableButton.setEnabled(table.isAvailable());
                selectedTableIds.remove(table.getId());
                selectedTableNumbers.remove(table.getTableNumber());
            } else {
                table.setSelected(true);
                holder.tableButton.setAlpha(0.7f);
                holder.tableButton.setTextColor(Color.BLUE);
                holder.tvTableStatus.setTextColor(Color.BLUE);
                holder.tvTableStatus.setText("Đã chọn");
                selectedTableIds.add(table.getId());
                selectedTableNumbers.add(table.getTableNumber());
            }

            selectedTableIds = getSelectedTableIds();
            selectedTableNumbers = getSelectedTableNumbers();

            if (listener != null) {
                listener.onTableSelectionChanged(selectedTableIds, selectedTableNumbers);
            }
        });
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
