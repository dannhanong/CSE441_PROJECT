package com.ktpm1.restaurant.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ktpm1.restaurant.R;

public class WebFragment extends Fragment {

    private static final String URL_KEY = "url_key";
    private WebView webView;
    private Toolbar toolbar;

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, url);  // Truyền URL vào bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout chứa WebView
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
//        toolbar = view.findViewById(R.id.toolbarWeb);
        webView = view.findViewById(R.id.webview); // Khởi tạo WebView từ layout

        // Cấu hình WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu trang yêu cầu

        // Mở URL trong WebView thay vì trình duyệt bên ngoài
        webView.setWebViewClient(new WebViewClient());

        // Nhận URL từ Bundle
        String url = getArguments() != null ? getArguments().getString(URL_KEY) : null;
        if (url != null) {
            webView.loadUrl(url);
        }

//        // Thiết lập Toolbar làm ActionBar
//        if (getActivity() != null) {
//            AppCompatActivity activity = (AppCompatActivity) getActivity();
//            activity.setSupportActionBar(toolbar);
//
//            if (activity.getSupportActionBar() != null) {
//                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Icon back tùy chỉnh
//                activity.getSupportActionBar().setTitle("Thanh toán đơn hàng");
//            }
//        }
//
//        // Cho phép Fragment lắng nghe sự kiện MenuItem
//        setHasOptionsMenu(true);
//
//        // Xử lý sự kiện nút back trong Fragment
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (shouldInterceptBackPress()) {
//                    Toast.makeText(getContext(), "Back pressed in CartFragment", Toast.LENGTH_SHORT).show();
//                } else {
//                    setEnabled(false); // Cho phép hệ thống xử lý sự kiện back
//                    requireActivity().onBackPressed();
//                }
//            }
//        });

        // Lắng nghe nút Back
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (webView.canGoBack()) {
                        webView.goBack(); // Quay lại trang trước trong WebView nếu có
                        return true;
                    } else {
                        // Nếu không thể quay lại trong WebView, quay lại Fragment đích và truyền dữ liệu
                        sendResultAndGoBack();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void sendResultAndGoBack() {
        // Tạo dữ liệu để gửi về Fragment trước
        Bundle result = new Bundle();
        result.putString("status", "success"); // Truyền dữ liệu status (ví dụ thành công)

        // Sử dụng setFragmentResult để gửi dữ liệu về Fragment trước
        getParentFragmentManager().setFragmentResult("requestKey", result);

        // Quay lại Fragment trước đó bằng popBackStack
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
        }
    }
}