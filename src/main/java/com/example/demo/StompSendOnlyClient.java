//Java 的 STOMP WebSocket 客戶端，它會自動連線到你的 Spring Boot WebSocket 伺服器
//只負責「發送訊息」（不訂閱、不接收訊息）。
//這種程式常見於自動化測試、機器人推播、後台通知等場景
package com.example.demo;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class StompSendOnlyClient {

	public static void main(String [] args) throws Exception{
		//url 是 WebSocket 伺服器的連線位址（端點）
		String url = "ws://localhost:8080/chat-websocket/websocket";
		//WebSocketStompClient 是 Spring 提供的 STOMP 客戶端
		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		//MappingJackson2MessageConverter 讓 Java 物件自動轉成 JSON 格式傳送
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
	
		//stompClient.connect(...)：連線到 WebSocket 伺服器
		ListenableFuture<StompSession> future = stompClient.connect(url, new StompSessionHandlerAdapter() {});
		StompSession session = future.get();//等待連線完成
		
		for(int i = 1; i <= 10; i++) {
			ChatMessage message = new ChatMessage();
			message.setFrom("AI");
			message.setContent("現在有大特價..." + i);
			session.send("/app/chat", message);
			System.out.println("訊息已發送: " + i);
			Thread.sleep(1000);
			
		}
		session.disconnect();
	}
}
