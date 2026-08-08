package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentRepositoryTest {

    private DocumentRepository repository;


    @BeforeEach
    void setup() {

        repository =
                new InMemoryDocumentRepository();
    }


    @Test
    void shouldSaveAndLoadDocument() {

        Document document =
                new Document(
                        "Java and Spring Boot"
                );

        repository.save(document);

        Document result =
                repository.findById(
                        document.getId()
                ).orElseThrow();

        assertEquals(
                document.getId(),
                result.getId()
        );

        assertEquals(
                "Java and Spring Boot",
                result.getContent()
        );
    }


    @Test
    void shouldDeleteDocument() {

        Document document =
                new Document(
                        "Java and Spring Boot"
                );

        repository.save(document);

        repository.delete(
                document.getId()
        );

        assertTrue(
                repository.findById(
                        document.getId()
                ).isEmpty()
        );
    }
}
