package ch.wiss.m294_doc_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenericDocumentServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private GenericDocumentService documentService;

    private GenericDocument testDocument;
    private final String COLLECTION_NAME = "test_collection";

    @BeforeEach
    void setUp() {
        testDocument = new GenericDocument();
        testDocument.setId("test123");
        Map<String, Object> content = new HashMap<>();
        content.put("title", "Test Document");
        content.put("description", "This is a test document");
        testDocument.setContent(content);
    }

    @Test
    void testSaveDocument() {
        // Arrange
        when(mongoTemplate.save(any(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(testDocument);

        // Act
        GenericDocument savedDocument = documentService.save(COLLECTION_NAME, testDocument);

        // Assert
        assertNotNull(savedDocument);
        assertEquals("test123", savedDocument.getId());
        assertEquals("Test Document", savedDocument.getContent().get("title"));
        verify(mongoTemplate).save(testDocument, COLLECTION_NAME);
    }

    @Test
    void testFindAllDocuments() {
        // Arrange
        GenericDocument doc2 = new GenericDocument();
        doc2.setId("test456");
        List<GenericDocument> documents = Arrays.asList(testDocument, doc2);
        when(mongoTemplate.findAll(GenericDocument.class, COLLECTION_NAME))
            .thenReturn(documents);

        // Act
        List<GenericDocument> foundDocuments = documentService.findAll(COLLECTION_NAME);

        // Assert
        assertEquals(2, foundDocuments.size());
        verify(mongoTemplate).findAll(GenericDocument.class, COLLECTION_NAME);
    }

    @Test
    void testFindByIdExisting() {
        // Arrange
        when(mongoTemplate.findById(eq("test123"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(testDocument);

        // Act
        Optional<GenericDocument> found = documentService.findById(COLLECTION_NAME, "test123");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("test123", found.get().getId());
    }

    @Test
    void testFindByIdNonExisting() {
        // Arrange
        when(mongoTemplate.findById(eq("nonexistent"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(null);

        // Act
        Optional<GenericDocument> found = documentService.findById(COLLECTION_NAME, "nonexistent");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteByIdExisting() {
        // Arrange
        when(mongoTemplate.findById(eq("test123"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(testDocument);
        doNothing().when(mongoTemplate).remove(any(GenericDocument.class), eq(COLLECTION_NAME));

        // Act
        boolean result = documentService.deleteById(COLLECTION_NAME, "test123");

        // Assert
        assertTrue(result);
        verify(mongoTemplate).remove(testDocument, COLLECTION_NAME);
    }

    @Test
    void testDeleteByIdNonExisting() {
        // Arrange
        when(mongoTemplate.findById(eq("nonexistent"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(null);

        // Act
        boolean result = documentService.deleteById(COLLECTION_NAME, "nonexistent");

        // Assert
        assertFalse(result);
        verify(mongoTemplate, never()).remove(any(GenericDocument.class), eq(COLLECTION_NAME));
    }

    @Test
    void testUpdateByIdExisting() {
        // Arrange
        GenericDocument updatedDoc = new GenericDocument();
        Map<String, Object> newContent = new HashMap<>();
        newContent.put("title", "Updated Title");
        updatedDoc.setContent(newContent);

        when(mongoTemplate.findById(eq("test123"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(testDocument);
        when(mongoTemplate.save(any(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(updatedDoc);

        // Act
        Optional<GenericDocument> result = documentService.updateById(COLLECTION_NAME, "test123", updatedDoc);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getContent().get("title"));
    }

    @Test
    void testUpdateByIdNonExisting() {
        // Arrange
        GenericDocument updatedDoc = new GenericDocument();
        when(mongoTemplate.findById(eq("nonexistent"), eq(GenericDocument.class), eq(COLLECTION_NAME)))
            .thenReturn(null);

        // Act
        Optional<GenericDocument> result = documentService.updateById(COLLECTION_NAME, "nonexistent", updatedDoc);

        // Assert
        assertFalse(result.isPresent());
        verify(mongoTemplate, never()).save(any(), any());
    }
}
