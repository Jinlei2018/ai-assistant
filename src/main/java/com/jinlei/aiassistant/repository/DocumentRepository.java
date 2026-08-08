package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    void save(Document document);

    Optional<Document> findById(String id);

    List<Document> findAll();

    void delete(String id);
}