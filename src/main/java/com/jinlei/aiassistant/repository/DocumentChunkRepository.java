package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.DocumentChunk;

import java.util.List;

public interface DocumentChunkRepository {

    void save(DocumentChunk chunk);

    List<DocumentChunk> findByDocumentId(String documentId);

    void deleteByDocumentId(String documentId);
}