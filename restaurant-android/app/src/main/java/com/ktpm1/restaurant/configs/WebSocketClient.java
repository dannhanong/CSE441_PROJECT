package com.ktpm1.restaurant.configs;

import org.json.JSONObject;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient extends WebSocketListener {
    private WebSocket webSocket;
    private WebSocketCallback callback;

    public WebSocketClient(WebSocketCallback callback) {
        this.callback = callback;
    }

    public void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://your-server-ip/ws/table-reservation").build();
        webSocket = client.newWebSocket(request, this);
    }

    @SneakyThrows
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // Nhận thông tin cập nhật từ server
        JSONObject jsonObject = new JSONObject(text);
        int tableId = jsonObject.getInt("tableId");
        String status = jsonObject.getString("status");

        // Gọi callback để cập nhật giao diện
        callback.onTableStatusUpdated(tableId, status);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        // Xử lý khi lỗi
    }

    // Interface callback
    public interface WebSocketCallback {
        void onTableStatusUpdated(int tableId, String status);
    }
}