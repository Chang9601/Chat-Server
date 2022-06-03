package com.cos.chatapp;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Document(collection = "chat")
public class Chat {
	
	@Id
	private String id;
	private String msg;
	private String sender; // 송신자
	private String receiver; // 수신자(귓속말)
	private Integer roomNum; // 방 번호
	
	private LocalDateTime creationTime;
}