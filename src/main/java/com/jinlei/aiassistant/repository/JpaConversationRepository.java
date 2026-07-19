package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.entity.chat.ConversationEntity;
import com.jinlei.aiassistant.entity.chat.MessageEntity;
import com.jinlei.aiassistant.mapper.ConversationMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public class JpaConversationRepository implements ConversationRepository {


    private final SpringDataConversationRepository repository;

    private final ConversationMapper mapper;


    public JpaConversationRepository(
            SpringDataConversationRepository repository,
            ConversationMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public Optional<Conversation> findById(
            String conversationId
    ) {

        return repository.findById(conversationId)
                .map(mapper::toModel);
    }


    @Override
    public void save(
            String conversationId,
            Conversation conversation
    ) {

        ConversationEntity entity =
                repository.findById(conversationId)
                        .orElseGet(() ->
                                new ConversationEntity(conversationId)
                        );


        conversation.getMessages()
                .stream()
                .filter(message -> message.getId() == null)
                .forEach(message ->
                        entity.addMessage(
                                mapper.toEntity(message)
                        )
                );

        ConversationEntity saved = repository.saveAndFlush(entity);

        mapper.updateIds(conversation, saved);
    }

    @Override
    public boolean exists(
            String conversationId
    ) {

        return repository.existsById(conversationId);
    }


    @Override
    public void delete(
            String conversationId
    ) {

        repository.deleteById(conversationId);
    }
}