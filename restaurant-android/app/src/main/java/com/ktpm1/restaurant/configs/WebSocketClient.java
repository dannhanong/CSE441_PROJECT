package com.ktpm1.restaurant.configs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient extends WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private WebSocket webSocket;

    public void connectWebSocket(String wsUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(wsUrl).build();
        webSocket = client.newWebSocket(request, this); // Khởi tạo WebSocket với listener này
        client.dispatcher().executorService().shutdown(); // Quản lý kết nối
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("Receiving : " + text);
    }

    @Override
    public void onOpen(WebSocket webSocket, okhttp3.Response response) {
        System.out.println("WebSocket opened!");
        // Gửi message thử nếu cần
        webSocket.send("Hello Server!");
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        t.printStackTrace();
    }

    public void closeWebSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
    }
}