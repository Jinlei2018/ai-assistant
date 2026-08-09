package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.rag.DocumentChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class JpaDocumentChunkRepositoryTest {

    @Autowired
    private SpringDataDocumentChunkRepository springDataRepository;

    private JpaDocumentChunkRepository repository;

    @BeforeEach
    void setup() {

        repository =
                new JpaDocumentChunkRepository(
                        springDataRepository
                );
    }

    @Test
    void shouldPersistAndLoadChunks() {

        DocumentChunk chunk =
                new DocumentChunk(
                        "chunk-1",
                        "document-1",
                        0,
                        "Java and Spring Boot"
                );

        repository.save(chunk);

        List<DocumentChunk> result =
                repository.findByDocumentId(
                        "document-1"
                );

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "chunk-1",
                result.get(0).getId()
        );

        assertEquals(
                "Java and Spring Boot",
                result.get(0).getContent()
        );
    }
}

