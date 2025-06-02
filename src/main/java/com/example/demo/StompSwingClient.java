/*這是一個基礎的 Swing + WebSocket 整合範例，實現了：
 * 圖形化聊天介面
 * WebSocket 連線管理
 * 訊息發送功能
 * 需補強訊息接收和錯誤處理才能用於正式環境。適合學習如何將桌面應用與即時通訊技術結合。
 * */

/*應用場景
 * 後台管理系統的訊息推播工具
 * 需要圖形介面的聊天室測試工具
 * 企業內部即時通知系統
 * */

package com.example.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
//1.介面設計 (UI Components)
public class StompSwingClient extends JFrame {
    private JTextField fromField;	// 用戶名稱輸入框
    private JTextArea messageArea;	// 聊天訊息顯示區
    private JTextField inputField;	// 訊息輸入框
    private JButton sendButton;		// 傳送按鈕
    private StompSession session; 	// WebSocket 連線 session
    
    
    public StompSwingClient() {
        setTitle("STOMP WebSocket Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fromField = new JTextField("後台", 10);
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        inputField = new JTextField(20);
        sendButton = new JButton("Send");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("From:"));
        topPanel.add(fromField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(inputField);
        bottomPanel.add(sendButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

    
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        connectWebSocket();
    }

    //2.連線機制 (WebSocket Connection)
    private void connectWebSocket() {
        try {
            String url = "ws://localhost:8080/chat-websocket/websocket";
            WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            ListenableFuture<StompSession> future = stompClient.connect(url, new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    StompSwingClient.this.session = session;
                    appendMessage("Connected to server.");
                }

                @Override
                public void handleTransportError(StompSession session, Throwable exception) {
                    appendMessage("Connection error: " + exception.getMessage());
                }
            });

            future.get();
        } catch (Exception ex) {
            appendMessage("Failed to connect: " + ex.getMessage());
        }
    }

    //3. 訊息傳送邏輯
    private void sendMessage() {
        if (session == null || !session.isConnected()) {
            appendMessage("Not connected.");
            return;
        }
        String from = fromField.getText().trim();
        String content = inputField.getText().trim();
        if (content.isEmpty()) {
            return;
        }
        ChatMessage message = new ChatMessage();
        message.setFrom(from);
        message.setContent(content);
        session.send("/app/chat", message);
        appendMessage("You: " + content);
        inputField.setText("");
    }

   
    private void appendMessage(String msg) {
        messageArea.append(msg + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StompSwingClient client = new StompSwingClient();
            client.setVisible(true);
        });
    }

}