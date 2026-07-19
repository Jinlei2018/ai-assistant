package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.entity.chat.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataConversationRepository extends JpaRepository<ConversationEntity, String> {

}