package com.ktpm1.restaurant.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class TableReservationHandler extends TextWebSocketHandler {

    // List để lưu giữ các session kết nối WebSocket
    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // Xử lý thông tin đặt bàn (bàn số mấy, trạng thái là gì)
        // Ví dụ: {"tableId": 5, "status": "occupied"}

        // Sau khi xử lý logic, gửi thông báo cho tất cả client
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(payload)); // Trả thông tin đặt bàn cho tất cả client
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
