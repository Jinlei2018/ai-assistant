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

        Conversation conversation = new Conversation();

        conversation.setSummary(entity.getSummary());
        conversation.setTitle(entity.getTitle());

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

    public void updateIds(
            Conversation conversation,
            ConversationEntity entity
    ) {

        for (int i = 0; i < conversation.getMessages().size(); i++) {

            Message message =
                    conversation.getMessages().get(i);

            MessageEntity messageEntity =
                    entity.getMessages().get(i);

            message.setId(
                    messageEntity.getId()
            );
        }
    }
}