package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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

import android.os.Looper;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.TableAdapter;
import com.ktpm1.restaurant.apis.BookingTableApi;
import com.ktpm1.restaurant.apis.OrderApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.configs.WebSocketClient;
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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.StompClient;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import ua.naiksoftware.stomp.Stomp;

public class TableFragment extends Fragment implements TableAdapter.OnTableSelectionChangedListener{
    private Long catalogId;
    private RecyclerView rcvTables;
    private TextView tvFreeTables;
    private TextView tvTotalTables, tvTimeSelected;
    private List<TableResponse> tableList;
    private List<Long> selectedTableIds;
    private List<String> selectedTableNumbers;
    private Calendar calendar;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private AlertDialog dialog;
    private Toolbar toolbar;
    private Button btnConfirm;
    private ImageView imgHelp;
    private Spinner additionalTimeSpinner;
    private HashMap<String, Integer> additionalTimeMap;
    private int additionalTime;
    private StompClient stompClient;
    private List<TableResponse> tableListAfter;

    public TableFragment() {
    }

    public TableFragment(Long catalogId) {
        this.catalogId = catalogId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
//        webSocketClient = new WebSocketClient();
//        webSocketClient.connectWebSocket("ws://192.168.1.10:8080/ws/websocket");

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.1.16:8080/ws/websocket");

        // Kết nối
        stompClient.connect();

        stompClient.topic("/topic/bookings").subscribe(message -> {
            Log.e("WebSocket", "Message received: " + message.getPayload());

            new Handler(Looper.getMainLooper()).post(() ->
                    updateTableStatus());
        });

        init(view);

        fetchTables();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stompClient != null) {
            stompClient.disconnect();  // Đóng kết nối WebSocket khi Activity bị hủy
        }
    }

    private void init(View view) {
        rcvTables = view.findViewById(R.id.rcv_tables_by_catalog);
        tvFreeTables = view.findViewById(R.id.txt_free_tables);
        tvTotalTables = view.findViewById(R.id.txt_total_tables);
        tvTimeSelected = view.findViewById(R.id.txt_time_selected);
        tableList = new ArrayList<>();
        selectedTableIds = new ArrayList<>();
        selectedTableNumbers = new ArrayList<>();
        rcvTables.setLayoutManager(new GridLayoutManager(getContext(), 3));
        btnConfirm = view.findViewById(R.id.btn_confirm_table);
        imgHelp = view.findViewById(R.id.img_help);

        toolbar = view.findViewById(R.id.toolbarTable);
        additionalTimeSpinner = view.findViewById(R.id.spinner_additional_time);

        additionalTimeMap = new HashMap<>();
        additionalTimeMap.put("Có sẵn", 0);
        additionalTimeMap.put("1 giờ", 1);
        additionalTimeMap.put("2 giờ", 2);
        additionalTimeMap.put("3 giờ", 3);
        additionalTimeMap.put("4 giờ", 4);
        additionalTimeMap.put("5 giờ", 5);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.additional_time_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        additionalTimeSpinner.setAdapter(adapter);

        additionalTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                int selectedTime = additionalTimeMap.get(selectedOption);
                additionalTime = selectedTime;

                fetchTables();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Kiểm tra lại thông tin đặt bàn");
            String selectedTables = selectedTableNumbers.stream().map(String::valueOf).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
            builder.setMessage("Bạn đã chọn bàn số " + selectedTables + ". Chắc chắn với lựa chọn này?");
            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                nextToCreateOrder();
                dialog.dismiss();
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        });

        imgHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Hướng dẫn chọn bàn");

            // Tạo SpannableStringBuilder để chứa văn bản với định dạng khác nhau
            SpannableStringBuilder message = new SpannableStringBuilder();

            // Phần đầu tiên của thông báo (không định dạng)
            message.append("Chọn ngày và giờ bạn muốn đặt bàn, sau đó chọn bàn bạn muốn đặt. " +
                    "Sau khi chọn xong, nhấn nút Xác nhận để hoàn tất đặt bàn.\n\n");

            // Phần "Chú ý:" được in nghiêng
            SpannableString notice = new SpannableString("Chú ý: \n");
            notice.setSpan(new StyleSpan(Typeface.BOLD), 0, notice.length(), 0);  // In nghiêng
            message.append(notice);

            // Phần dưới "Chú ý" có màu đỏ
            SpannableString redText1 = new SpannableString("\t(*) Bạn chỉ có thể đặt bàn từ 7:00 đến 19:30 trong ngày\n");
            SpannableString redText2 = new SpannableString("\t(*) Mỗi bàn mặc định có thời gian đặt là 2 tiếng, nếu bạn muốn đặt thời gian khác, " +
                    "hãy chọn thêm thời gian phù hợp với nhu cầu, tránh làm ảnh hưởng đến những khách hàng khác.");

            // Áp dụng màu đỏ cho văn bản
            redText1.setSpan(new ForegroundColorSpan(Color.RED), 0, redText1.length(), 0);
            redText1.setSpan(new StyleSpan(Typeface.ITALIC), 0, redText1.length(), 0);
            redText2.setSpan(new ForegroundColorSpan(Color.RED), 0, redText2.length(), 0);
            redText2.setSpan(new StyleSpan(Typeface.ITALIC), 0, redText2.length(), 0);

            // Thêm phần văn bản có màu đỏ vào thông báo
            message.append(redText1);
            message.append(redText2);

            // Đặt nội dung cho AlertDialog
            builder.setMessage(message);

            // Nút "Đã hiểu"
            builder.setPositiveButton("Đã hiểu", (dialog, which) -> dialog.dismiss());

            // Hiển thị AlertDialog
            builder.show();
        });
    }

    private void showDateTimePicker() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_time_prompt, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
//        TextView timePrompt = dialogView.findViewById(R.id.timePrompt);
//        timePrompt.setText("Chọn ngày và giờ để xem trạng thái bàn");
//        dialog = builder.create();
//        Window window = dialog.getWindow();
//        if (window != null) {
//            // Đặt các thuộc tính cho window
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(window.getAttributes());
//            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL; // Đặt vị trí ở góc trên
//            layoutParams.y = 50; // Khoảng cách từ trên cùng (thay đổi giá trị này tùy theo ý muốn)
//            window.setAttributes(layoutParams);
//
//            layoutParams.dimAmount = 1.0f; // Độ mờ của nền (0.0f: không mờ, 1.0f: mờ hoàn toàn)
//            window.setAttributes(layoutParams);
//
//            // Cho phép làm mờ nền
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            window.setWindowAnimations(android.R.style.Animation_Dialog);
//        }

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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
//        dialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            // Kiểm tra giờ được chọn có nằm ngoài phạm vi hợp lệ không
            if (hourOfDay < 7 || (hourOfDay == 19 && minute > 30) || hourOfDay > 19) {
                // Hiển thị thông báo nếu giờ không hợp lệ
                Snackbar snackbar = Snackbar.make(getView(), "Chỉ có thể chọn thời gian từ 7:00 đến 19:30", 5000);

                // Truy cập vào TextView của Snackbar để thay đổi màu chữ
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.green));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();

                showTimePicker();
            } else {
                // Lưu giờ được chọn sau khi đã kiểm tra
                selectedHour = hourOfDay;
                selectedMinute = minute;

                // Hiển thị thời gian đã chọn
                String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                tvTimeSelected.append(" " + formattedTime);

                // Gọi API để lấy trạng thái bàn sau khi người dùng chọn thời gian
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
        Call<List<TableResponse>> call = bookingTableApi.getStatusTableByAvailable(formattedDateTime, additionalTime,catalogId);
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

    private void updateTableStatus() {
        String formattedDateTime = getFormattedDateTime();
        BookingTableApi bookingTableApi = ApiClient.getClient().create(BookingTableApi.class);
        Call<List<TableResponse>> call = bookingTableApi.getStatusTableByAvailable(formattedDateTime, additionalTime,catalogId);
        call.enqueue(new Callback<List<TableResponse>>() {
            @Override
            public void onResponse(Call<List<TableResponse>> call, Response<List<TableResponse>> response) {
                if (response.isSuccessful()) {
                    tableListAfter = response.body();

                    if (tableListAfter != null) {
                        for (int i = 0; i < tableList.size(); i++) {
                            TableResponse oldTable = tableList.get(i);
                            TableResponse newTable = tableListAfter.get(i);

                            if (oldTable.isAvailable() != newTable.isAvailable()) {
                                tableList.set(i, newTable);
                                rcvTables.getAdapter().notifyItemChanged(i);
                                tvFreeTables.setText(String.valueOf(tableList.stream().map(TableResponse::isAvailable).filter(available -> available).count()));
                            }
                        }
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
    public void onTableSelectionChanged(List<Long> selectedTableIds, List<String> selectedTableNumbers) {
        this.selectedTableIds = selectedTableIds;
        this.selectedTableNumbers = selectedTableNumbers;
        if (getFormattedDateTime().equals("") == false && !selectedTableIds.isEmpty()) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
    }

    private void createBookingTable(boolean addCart) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        String formattedDateTime = getFormattedDateTime();

        BookingTableApi bookingTableApi = ApiClient.getClient().create(BookingTableApi.class);
        BookingTableRequest bookingTableRequest = new BookingTableRequest(selectedTableIds, formattedDateTime, additionalTime, addCart);

        Call<ResponseMessage> call = bookingTableApi.createBookingTable("Bearer " + token, bookingTableRequest);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
//                    nextToCreateOrder();
                    selectedTableIds.clear();
                    selectedTableNumbers.clear();
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
        if (selectedTableIds != null && selectedTableIds.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // Sử dụng layout tùy chỉnh cho AlertDialog
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
            builder.setView(dialogView);

            // Tham chiếu đến các thành phần trong layout tùy chỉnh
            TextView btnCartDetail = dialogView.findViewById(R.id.detailCartOnDialog);
            TextView btnConfirm = dialogView.findViewById(R.id.btnConFirmOrderTAndF);
            TextView btnDecline = dialogView.findViewById(R.id.btnRefuseOrderTAndF);

            // Tạo AlertDialog
            AlertDialog dialog = builder.create();

            btnCartDetail.setOnClickListener(v -> {
                goToCartFragment(new BookingTableRequest(selectedTableIds, getFormattedDateTime(), additionalTime, true));
                dialog.dismiss();
            });

            // Sự kiện cho nút "Xác nhận"
            btnConfirm.setOnClickListener(v -> {
                callApiToAddItemsToCart();
                dialog.dismiss(); // Đóng hộp thoại sau khi thực hiện hành động
            });

            // Sự kiện cho nút "Từ chối"
            btnDecline.setOnClickListener(v -> {
                callApiToDeclineItems();
                dialog.dismiss(); // Đóng hộp thoại sau khi thực hiện hành động
            });

            // Hiển thị hộp thoại
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Đặt bàn thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void callApiToDeclineItems() {
        createBookingTable(false);
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
        Call<VNPayMessage> call = orderApi.createOrder("Bearer " + token,
                new BookingTableRequest(selectedTableIds, getFormattedDateTime(), additionalTime, true));
        call.enqueue(new Callback<VNPayMessage>() {
            @Override
            public void onResponse(Call<VNPayMessage> call, Response<VNPayMessage> response) {
                if (response.isSuccessful()) {
                    String url = response.body().getVnpayUrl();
                    // Tạo instance WebFragment và truyền URL
                    WebFragment webFragment = WebFragment.newInstance(url);

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

    private void goToCartFragment(BookingTableRequest bookingRequest) {
        CartFragment cartFragment = new CartFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("bookingRequest", bookingRequest);
        cartFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cartFragment)
                .addToBackStack(null)
                .commit();
    }
}
