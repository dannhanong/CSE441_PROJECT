package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.TableAdapter;
import com.ktpm1.restaurant.apis.BookingTableApi;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.BookingTableRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.dtos.responses.TableResponse;
import com.ktpm1.restaurant.dtos.responses.VNPayMessage;
import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.utils.DateUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment implements TableAdapter.OnTableSelectionChangedListener{
    private Long catalogId;
    private RecyclerView rcvTables;
    private TextView tvFreeTables;
    private TextView tvTotalTables, tvTimeSelected;
    private List<TableResponse> tableList;
    private List<Long> selectedTableIds;
    private Calendar calendar;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private AlertDialog dialog;
    private Toolbar toolbar;
    private Button btnConfirm;

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
        tvTimeSelected = view.findViewById(R.id.txt_time_selected);
        tableList = new ArrayList<>();
        selectedTableIds = new ArrayList<>();
        rcvTables.setLayoutManager(new GridLayoutManager(getContext(), 3));
        btnConfirm = view.findViewById(R.id.btn_confirm_table);

        toolbar = view.findViewById(R.id.toolbarTable);

//        btnConfirm.setEnabled(false);

        // Thiết lập Toolbar làm ActionBar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Icon back tùy chỉnh
                activity.getSupportActionBar().setTitle("Thông tin bàn");
            }
        }

        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldInterceptBackPress()) {
                    Toast.makeText(getContext(), "Back pressed in CartFragment", Toast.LENGTH_SHORT).show();
                } else {
                    setEnabled(false); // Cho phép hệ thống xử lý sự kiện back
                    requireActivity().onBackPressed();
                }
            }
        });

        TableAdapter tableAdapter = new TableAdapter(tableList);
        rcvTables.setAdapter(tableAdapter);

        calendar = Calendar.getInstance();

        showDateTimePicker();

        tvTimeSelected.setOnClickListener(v -> {
            selectedYear = -1;
            selectedMonth = -1;
            selectedDay = -1;
            selectedHour = -1;
            selectedMinute = -1;

            btnConfirm.setEnabled(false);

            showDateTimePicker();
            // Xóa thông tin thời gian đã chọn
            tvTimeSelected.setText("Thời gian chọn: ");
        });

        if (getFormattedDateTime() == null || selectedTableIds.isEmpty()) {
            btnConfirm.setEnabled(false);
        }

        btnConfirm.setOnClickListener(view1 -> {
//            Bundle arguments = getArguments();
            createBookingTable();
        });

    }

    private void showDateTimePicker() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_time_prompt, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        TextView timePrompt = dialogView.findViewById(R.id.timePrompt);
        timePrompt.setText("Chọn ngày và giờ để xem trạng thái bàn");
        dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            // Đặt các thuộc tính cho window
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL; // Đặt vị trí ở góc trên
            layoutParams.y = 50; // Khoảng cách từ trên cùng (thay đổi giá trị này tùy theo ý muốn)
            window.setAttributes(layoutParams);

            layoutParams.dimAmount = 1.0f; // Độ mờ của nền (0.0f: không mờ, 1.0f: mờ hoàn toàn)
            window.setAttributes(layoutParams);

            // Cho phép làm mờ nền
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setWindowAnimations(android.R.style.Animation_Dialog);
        }

        calendar = Calendar.getInstance();
//        selectedYear = calendar.get(Calendar.YEAR);
//        selectedMonth = calendar.get(Calendar.MONTH);
//        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
//        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
//        selectedMinute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            // Lưu ngày được chọn
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;

//            Toast.makeText(getContext(), "Date: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            tvTimeSelected.append(dayOfMonth + "/" + (month + 1) + "/" + year);

            showTimePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        dialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Lưu giờ được chọn
                selectedHour = hourOfDay;
                selectedMinute = minute;

//                Toast.makeText(getContext(), "Time: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                tvTimeSelected.append(" " + hourOfDay + ":" + minute);
                dialog.dismiss();

                // Sau khi người dùng chọn ngày và giờ, gọi API để lấy trạng thái bàn
                fetchTables();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private String getFormattedDateTime() {
        if (selectedYear == -1 || selectedMonth == -1 || selectedDay == -1 || selectedHour == -1 || selectedMinute == -1) {
            return "";
        }
        // Chuyển đổi ngày giờ thành định dạng LocalDateTime (yyyy-MM-dd'T'HH:mm:ss)
        return String.format("%04d-%02d-%02dT%02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quay lại Fragment hoặc Activity trước đó
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức kiểm tra điều kiện xử lý nút back
    private boolean shouldInterceptBackPress() {
        return false; // Trả về false nếu không cần chặn back
    }

    private void fetchTables() {
        String formattedDateTime = getFormattedDateTime();
        BookingTableApi bookingTableApi = ApiClient.getClient().create(BookingTableApi.class);
        Call<List<TableResponse>> call = bookingTableApi.getStatusTableByAvailable(formattedDateTime, 0,catalogId);
        call.enqueue(new Callback<List<TableResponse>>() {
            @Override
            public void onResponse(Call<List<TableResponse>> call, Response<List<TableResponse>> response) {
                if (response.isSuccessful()) {
                    tableList = response.body();
                    if (tableList != null) {
                        TableAdapter tableAdapter = new TableAdapter(tableList, TableFragment.this);
                        rcvTables.setAdapter(tableAdapter);
                        tvFreeTables.setText(String.valueOf(tableList.stream().map(TableResponse::isAvailable).filter(available -> available).count()));
                        tvTotalTables.setText(String.valueOf(tableList.size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TableResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onTableSelectionChanged(List<Long> selectedTableIds) {
        this.selectedTableIds = selectedTableIds;
        if (getFormattedDateTime().equals("") == false && !selectedTableIds.isEmpty()) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
    }

    private void createBookingTable() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        String formattedDateTime = getFormattedDateTime();

        BookingTableApi bookingTableApi = ApiClient.getClient().create(BookingTableApi.class);
        BookingTableRequest bookingTableRequest = new BookingTableRequest(selectedTableIds, formattedDateTime, 0); // Gửi chuỗi định dạng

        Call<ResponseMessage> call = bookingTableApi.createBookingTable("Bearer " + token, bookingTableRequest);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    nextToCreateOrder();
                } else {
                    Toast.makeText(getContext(), "Đặt bàn thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void nextToCreateOrder() {
        if (selectedTableIds != null) {
            if (selectedTableIds.size() > 0) {
                // Tạo hộp thoại AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thêm món trong giỏ hàng");
                builder.setMessage("Bạn có muốn thêm các món trong giỏ hàng vào không?");

                // Nút Xác nhận
                builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                    // Gọi API khi người dùng chọn "Xác nhận"
                    callApiToAddItemsToCart();
                });

                // Nút Từ chối
                builder.setNegativeButton("Từ chối", (dialog, which) -> {
                    // Gọi API khi người dùng chọn "Từ chối"
                    callApiToDeclineItems();
                });

                // Hiển thị hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "Đặt bàn thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callApiToDeclineItems() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        OrderApi orderApi = ApiClient.getClient().create(OrderApi.class);
        Call<List<Order>> call = orderApi.createOrderTableOnly("Bearer " + token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đặt bàn thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Đặt bàn thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void callApiToAddItemsToCart() {
        OrderApi orderApi = ApiClient.getClient().create(OrderApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<VNPayMessage> call = orderApi.createOrder("Bearer " + token);
        call.enqueue(new Callback<VNPayMessage>() {
            @Override
            public void onResponse(Call<VNPayMessage> call, Response<VNPayMessage> response) {
                if (response.isSuccessful()) {
                    String url = response.body().getVnpayUrl(); // Lấy URL từ response

                    // Tạo instance WebFragment và truyền URL
                    WebFragment webFragment = WebFragment.newInstance(url);

                    // Thay thế Fragment hiện tại bằng WebFragment để hiển thị trang VNPay
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, webFragment) // R.id.fragment_container là container của fragment trong Activity của bạn
                            .addToBackStack(null) // Thêm vào BackStack để có thể quay lại
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Đặt bàn thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VNPayMessage> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
