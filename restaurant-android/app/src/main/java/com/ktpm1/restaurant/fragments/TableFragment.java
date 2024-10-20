package com.ktpm1.restaurant.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.TableAdapter;
import com.ktpm1.restaurant.apis.BookingTableApi;
import com.ktpm1.restaurant.apis.TableApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.models.Table;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {
    private Long catalogId;
    private RecyclerView rcvTables;
    private TextView tvFreeTables;
    private TextView tvTotalTables;
    private List<Table> tableList;
    private Calendar calendar;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    public TableFragment() {
    }

    public TableFragment(Long catalogId) {
        this.catalogId = catalogId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        init(view);

        fetchTables();

        return view;
    }

    private void init(View view) {
        rcvTables = view.findViewById(R.id.rcv_tables_by_catalog);
        tvFreeTables = view.findViewById(R.id.txt_free_tables);
        tvTotalTables = view.findViewById(R.id.txt_total_tables);
        tableList = new ArrayList<>();
        rcvTables.setLayoutManager(new GridLayoutManager(getContext(), 3));

        TableAdapter tableAdapter = new TableAdapter(tableList);
        rcvTables.setAdapter(tableAdapter);

        calendar = Calendar.getInstance();

        showDateTimePicker();

    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Lưu ngày được chọn
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                Toast.makeText(getContext(), "Date: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                showTimePicker();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Lưu giờ được chọn
                selectedHour = hourOfDay;
                selectedMinute = minute;

                Toast.makeText(getContext(), "Time: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

                // Sau khi người dùng chọn ngày và giờ, gọi API để lấy trạng thái bàn
                fetchTables();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private String getFormattedDateTime() {
        // Chuyển đổi ngày giờ thành định dạng LocalDateTime (yyyy-MM-dd'T'HH:mm:ss)
        return String.format("%04d-%02d-%02dT%02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
    }

    private void fetchTables() {
        String formattedDateTime = getFormattedDateTime();
        BookingTableApi bookingTableApi = ApiClient.getClient().create(BookingTableApi.class);
        Call<List<Table>> call = bookingTableApi.getStatusTableByAvailable(formattedDateTime, 0,catalogId);
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if (response.isSuccessful()) {
                    tableList = response.body();
                    if (tableList != null) {
                        TableAdapter tableAdapter = new TableAdapter(tableList);
                        rcvTables.setAdapter(tableAdapter);
                        tvFreeTables.setText(String.valueOf(tableList.stream().map(Table::isAvailable).filter(available -> available).count()));
                        tvTotalTables.setText(String.valueOf(tableList.size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
