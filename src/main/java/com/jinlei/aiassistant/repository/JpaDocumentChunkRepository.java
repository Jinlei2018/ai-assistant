package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.DocumentChunk;
import com.jinlei.aiassistant.entity.rag.DocumentChunkEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaDocumentChunkRepository
        implements DocumentChunkRepository {

    private final SpringDataDocumentChunkRepository repository;

    public JpaDocumentChunkRepository(
            SpringDataDocumentChunkRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(DocumentChunk chunk) {

        DocumentChunkEntity entity =
                new DocumentChunkEntity(
                        chunk.getId(),
                        chunk.getDocumentId(),
                        chunk.getIndex(),
                        chunk.getContent()
                );

        repository.saveAndFlush(entity);
    }

    @Override
    public List<DocumentChunk> findByDocumentId(
            String documentId
    ) {

        return repository
                .findByDocumentIdOrderByChunkIndex(documentId)
                .stream()
                .map(entity ->
                        new DocumentChunk(
                                entity.getId(),
                                entity.getDocumentId(),
                                entity.getChunkIndex(),
                                entity.getContent()
                        )
                )
                .toList();
    }

    @Override
    public void deleteByDocumentId(
            String documentId
    ) {

        List<DocumentChunkEntity> entities =
                repository
                        .findByDocumentIdOrderByChunkIndex(
                                documentId
                        );

        repository.deleteAll(entities);
    }
}
