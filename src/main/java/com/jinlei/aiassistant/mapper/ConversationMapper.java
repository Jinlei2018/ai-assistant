package com.jinlei.aiassistant.mapper;

import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.entity.chat.ConversationEntity;
import org.springframework.stereotype.Component;


@Component
public class ConversationMapper {


    public Conversation toModel(
            ConversationEntity entity
    ) {

        return new Conversation();
    }


    public ConversationEntity toEntity(
            String id,
            Conversation conversation
    ) {

        return new ConversationEntity(id);
    }
}