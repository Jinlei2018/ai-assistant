package com.jinlei.aiassistant.repository.jpa;

import com.jinlei.aiassistant.model.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConversationRepository extends JpaRepository<ConversationEntity, String> {

}