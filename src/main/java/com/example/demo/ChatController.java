/*
 * 角色：WebSocket 訊息的主要處理器。
 * 功能：用 @Controller、@MessageMapping 處理來自前端的聊天訊息，並用 @SendTo 廣播給所有訂閱者。
 * 說明：這是 STOMP 架構下的高階 Controller，讓你能用物件（如 ChatMessage）直接收發訊息，無需處理底層細節。
 */


package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public ChatMessage send(ChatMessage message) {
		return message;
	}
	
	@MessageMapping("/chat2")
	@SendTo("/topic/messages2")
	public ChatMessage send2(ChatMessage message) {
		return message;
	}
}
