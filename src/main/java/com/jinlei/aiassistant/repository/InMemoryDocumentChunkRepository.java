package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.DocumentChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDocumentChunkRepository
        implements DocumentChunkRepository {

    private final Map<String, DocumentChunk> chunks =
            new ConcurrentHashMap<>();

    @Override
    public void save(DocumentChunk chunk) {
        chunks.put(
                chunk.getId(),
                chunk
        );
    }

    @Override
    public List<DocumentChunk> findByDocumentId(
            String documentId
    ) {
        return chunks.values()
                .stream()
                .filter(chunk ->
                        chunk.getDocumentId()
                                .equals(documentId)
                )
                .sorted(
                        (a, b) ->
                                Integer.compare(
                                        a.getIndex(),
                                        b.getIndex()
                                )
                )
                .toList();
    }

    @Override
    public void deleteByDocumentId(
            String documentId
    ) {
        chunks.values()
                .removeIf(chunk ->
                        chunk.getDocumentId()
                                .equals(documentId)
                );
    }
}