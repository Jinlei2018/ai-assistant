package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.domain.rag.Document;
import com.jinlei.aiassistant.domain.rag.DocumentChunk;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentChunkerTest {

    private final DocumentChunker chunker =
            new DocumentChunker();


    @Test
    void shouldSplitDocumentIntoChunks() {

        String content =
                "a".repeat(1200);

        Document document =
                new Document(content);

        List<DocumentChunk> chunks =
                chunker.chunk(document);

        assertEquals(
                3,
                chunks.size()
        );

        assertEquals(
                500,
                chunks.get(0)
                        .getContent()
                        .length()
        );

        assertEquals(
                500,
                chunks.get(1)
                        .getContent()
                        .length()
        );

        assertEquals(
                200,
                chunks.get(2)
                        .getContent()
                        .length()
        );
    }


    @Test
    void shouldKeepDocumentIdOnEveryChunk() {

        Document document =
                new Document(
                        "Java and Spring Boot"
                );

        List<DocumentChunk> chunks =
                chunker.chunk(document);

        assertEquals(
                document.getId(),
                chunks.get(0)
                        .getDocumentId()
        );
    }
}

