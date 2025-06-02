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
	//當前端發送訊息到 /app/chat 時會進入這個方法
	//對應到index.html的後路徑--stompClient.send("/app/chat", {}, JSON.stringify({'from' : from, 'content' : content}));
	@MessageMapping("/chat")	//有訊息到/app/chat(監聽有沒有訊息)
	
	//方法回傳的訊息會廣播到 /topic/messages 主題，所有訂閱者都會收到
	//對應到index.html--stompClient.subscribe('/topic/messages', function(message){
	@SendTo("/topic/messages")	//訂閱一個主題/topic，主題叫做:/messages(有訊息就傳到有訂閱messages的人)
	public ChatMessage send(ChatMessage message) {
		//// 直接把收到的訊息回傳出去
		return message;
	}
	
	@MessageMapping("/chat2")
	@SendTo("/topic/messages2")
	public ChatMessage send2(ChatMessage message) {
		return message;
	}
}
