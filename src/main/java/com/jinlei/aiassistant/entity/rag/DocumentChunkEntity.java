package com.jinlei.aiassistant.entity.rag;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_chunks")
public class DocumentChunkEntity {

    @Id
    private String id;

    private String documentId;

    private int chunkIndex;

    @jakarta.persistence.Column(
            columnDefinition = "TEXT"
    )
    private String content;

    protected DocumentChunkEntity() {
    }

    public DocumentChunkEntity(
            String id,
            String documentId,
            int chunkIndex,
            String content
    ) {
        this.id = id;
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }
}
