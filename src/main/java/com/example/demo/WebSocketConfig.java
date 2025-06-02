/*
 * WebSocketConfig.java
 * 角色：WebSocket 與 STOMP 的組態設定檔。
 * 功能：設定 WebSocket 端點（如 /chat-websocket）、STOMP 主題（如 /topic）、應用前綴（如 /app）。
 * 說明：用 @EnableWebSocketMessageBroker 啟用 STOMP 訊息代理，讓你的應用支援主題訂閱、廣播等功能。
*/

package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//WebSocket的配置資訊
@Configuration
@EnableWebSocketMessageBroker //啟用 STOMP 協議的 WebSocket 支援
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	//定義訊息路由規則
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//設定訊息代理前綴，處理廣播(所有發送到 /topic 開頭的訊息都會被廣播(例如:/topic/messages))
		registry.enableSimpleBroker("/topic");
		//設定應用程式訊息前綴(前端發送到 /app 開頭的訊息會被路由到對應的 @MessageMapping("/chat")方法)
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 註冊 WebSocket 端點，支援 SockJS
		//index.html裡面的建立連線--var socket = new SockJS('/chat-websocket');
		registry.addEndpoint("/chat-websocket").withSockJS();	//WebSocket端點
	}

	

	
}
