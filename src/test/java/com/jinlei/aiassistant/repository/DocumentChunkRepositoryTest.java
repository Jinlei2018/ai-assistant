package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentChunkRepositoryTest {

    private DocumentChunkRepository repository;

    @BeforeEach
    void setup() {
        repository =
                new InMemoryDocumentChunkRepository();
    }

    @Test
    void shouldSaveAndFindChunksByDocumentId() {

        DocumentChunk first =
                new DocumentChunk(
                        "chunk-1",
                        "document-1",
                        0,
                        "First chunk"
                );

        DocumentChunk second =
                new DocumentChunk(
                        "chunk-2",
                        "document-1",
                        1,
                        "Second chunk"
                );

        repository.save(first);
        repository.save(second);

        List<DocumentChunk> result =
                repository.findByDocumentId(
                        "document-1"
                );

        assertEquals(2, result.size());

        assertEquals(
                "First chunk",
                result.get(0).getContent()
        );

        assertEquals(
                "Second chunk",
                result.get(1).getContent()
        );
    }

    @Test
    void shouldDeleteChunksByDocumentId() {

        DocumentChunk chunk =
                new DocumentChunk(
                        "chunk-1",
                        "document-1",
                        0,
                        "First chunk"
                );

        repository.save(chunk);

        repository.deleteByDocumentId(
                "document-1"
        );

        assertTrue(
                repository
                        .findByDocumentId("document-1")
                        .isEmpty()
        );
    }

    @Test
    void shouldNotReturnChunksFromAnotherDocument() {

        repository.save(
                new DocumentChunk(
                        "chunk-1",
                        "document-1",
                        0,
                        "Document one"
                )
        );

        repository.save(
                new DocumentChunk(
                        "chunk-2",
                        "document-2",
                        0,
                        "Document two"
                )
        );

        List<DocumentChunk> result =
                repository.findByDocumentId(
                        "document-1"
                );

        assertEquals(1, result.size());

        assertEquals(
                "Document one",
                result.get(0).getContent()
        );
    }
}