package com.cos.chatapp;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
	
	@Tailable // 커서를 닫지 않고 유지
	@Query("{ sender: ?0, receiver: ?1 }")
	Flux<Chat> mFindBySender(String sender, String receiver); // Flux = 흐름, response를 유지하면서 데이터 계속 흘려보내기	
	
	@Tailable // 커서를 닫지 않고 유지
	@Query("{ roomNum: ?0 }")
	Flux<Chat> mFindByRoomNum(Integer roomNum);	
}