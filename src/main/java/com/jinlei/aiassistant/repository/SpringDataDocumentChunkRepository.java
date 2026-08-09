package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.entity.rag.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataDocumentChunkRepository
        extends JpaRepository<DocumentChunkEntity, String> {

    List<DocumentChunkEntity> findByDocumentIdOrderByChunkIndex(
            String documentId
    );
}
