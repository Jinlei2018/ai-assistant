package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDocumentRepository implements DocumentRepository {

    private final Map<String, Document> documents =
            new ConcurrentHashMap<>();


    @Override
    public void save(Document document) {

        documents.put(
                document.getId(),
                document
        );
    }


    @Override
    public Optional<Document> findById(String id) {

        return Optional.ofNullable(
                documents.get(id)
        );
    }


    @Override
    public List<Document> findAll() {

        return new ArrayList<>(
                documents.values()
        );
    }


    @Override
    public void delete(String id) {

        documents.remove(id);
    }
}