package com.jinlei.aiassistant.domain.rag;

public class DocumentChunk {

    private final String id;

    private final String documentId;

    private final int index;

    private final String content;


    public DocumentChunk(
            String id,
            String documentId,
            int index,
            String content
    ) {
        this.id = id;
        this.documentId = documentId;
        this.index = index;
        this.content = content;
    }


    public String getId() {
        return id;
    }


    public String getDocumentId() {
        return documentId;
    }


    public int getIndex() {
        return index;
    }


    public String getContent() {
        return content;
    }
}

