package com.jinlei.aiassistant.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    private String id;

    private Instant createdAt;

    @Column(length = 4000)
    private String summary;

    @OneToMany(
            mappedBy = "conversation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<MessageEntity> messages = new ArrayList<>();


    protected ConversationEntity() {
    }


    public ConversationEntity(String id) {
        this.id = id;
        this.createdAt = Instant.now();
    }


    public void addMessage(MessageEntity message) {

        messages.add(message);
        message.setConversation(this);

    }


}