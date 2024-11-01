package com.ktpm1.restaurant.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import java.util.List;

public class SelectedTableAdapter extends RecyclerView.Adapter<SelectedTableAdapter.SelectedTableViewHolder> {

    private List<TableResponse> selectedTableList;

    public SelectedTableAdapter(List<TableResponse> selectedTableList) {
        this.selectedTableList = selectedTableList;
    }

    @NonNull
    @Override
    public SelectedTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new SelectedTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedTableViewHolder holder, int position) {
        TableResponse table = selectedTableList.get(position);

        // Thiết lập thông tin bàn
        holder.tableButton.setText(table.getTableNumber());

        // Luôn hiển thị trạng thái "Đã chọn"
        holder.tableButton.setAlpha(0.7f);
        holder.tableButton.setTextColor(Color.BLUE);
        holder.tvTableStatus.setText("Đã chọn");
        holder.tvTableStatus.setTextColor(Color.BLUE);
        holder.tableButton.setEnabled(false);  // Vô hiệu hóa nút để tránh tương tác
    }

    @Override
    public int getItemCount() {
        return selectedTableList.size();
    }

    public static class SelectedTableViewHolder extends RecyclerView.ViewHolder {
        Button tableButton;
        TextView tvTableStatus;

        public SelectedTableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableButton = itemView.findViewById(R.id.btn_tableNumber);
            tvTableStatus = itemView.findViewById(R.id.tv_tableStatus);
        }
    }
}
