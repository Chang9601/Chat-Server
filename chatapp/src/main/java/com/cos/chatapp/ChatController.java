package com.cos.chatapp;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class ChatController {
	
	private final ChatRepository chatRepository;
	
	public ChatController(ChatRepository chatRepository) {
		this.chatRepository = chatRepository;
	}
	
	// 귓속말
	@CrossOrigin // 자바스크립트 요청 허용(CORS)
	@GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
		return chatRepository.mFindBySender(sender, receiver)
			.subscribeOn(Schedulers.boundedElastic());
	}

	@CrossOrigin
	@GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum) {
		return chatRepository.mFindByRoomNum(roomNum)
			.subscribeOn(Schedulers.boundedElastic());
	}
	
	@CrossOrigin // 자바스크립트 요청 허용(CORS)
	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat) {
		chat.setCreationTime(LocalDateTime.now());
		return chatRepository.save(chat); // Object 반환 시 자동으로 JSON 변환(MessageConverter)
	}
}