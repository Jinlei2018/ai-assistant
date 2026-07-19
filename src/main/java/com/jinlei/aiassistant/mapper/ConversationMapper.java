package com.jinlei.aiassistant.mapper;

import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.entity.chat.ConversationEntity;
import com.jinlei.aiassistant.entity.chat.MessageEntity;
import org.springframework.stereotype.Component;


@Component
public class ConversationMapper {


    public Conversation toModel(
            ConversationEntity entity
    ) {

        Conversation conversation =
                new Conversation();


        entity.getMessages()
                .forEach(message ->
                        conversation.addMessage(
                                new Message(
                                        message.getId(),
                                        message.getRole(),
                                        message.getContent()
                                )
                        )
                );


        return conversation;
    }


    public MessageEntity toEntity(
            Message message
    ) {

        return new MessageEntity(
                message.getRole(),
                message.getContent()
        );
    }
}