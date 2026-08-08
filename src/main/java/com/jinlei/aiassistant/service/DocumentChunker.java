package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.domain.rag.Document;
import com.jinlei.aiassistant.domain.rag.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentChunker {

    private static final int CHUNK_SIZE = 500;


    public List<DocumentChunk> chunk(
            Document document
    ) {

        String content =
                document.getContent();

        List<DocumentChunk> chunks =
                new ArrayList<>();

        int index = 0;

        for (int start = 0;
             start < content.length();
             start += CHUNK_SIZE) {

            int end =
                    Math.min(
                            start + CHUNK_SIZE,
                            content.length()
                    );

            String chunkContent =
                    content.substring(
                            start,
                            end
                    );

            chunks.add(
                    new DocumentChunk(
                            UUID.randomUUID().toString(),
                            document.getId(),
                            index++,
                            chunkContent
                    )
            );
        }

        return chunks;
    }
}
