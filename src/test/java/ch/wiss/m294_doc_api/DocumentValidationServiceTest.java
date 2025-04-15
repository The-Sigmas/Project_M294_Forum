package ch.wiss.m294_doc_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentValidationServiceTest {

    @Mock
    private DocumentMetadataService metadataService;

    @Spy
    private DocumentContentValidator contentValidator;

    @InjectMocks
    private DocumentValidationService validationService;

    private GenericDocument testDocument;

    @BeforeEach
    void setUp() {
        testDocument = new GenericDocument();
        Map<String, Object> content = new HashMap<>();
        content.put("title", "Test Document");
        content.put("content", "This is a test document");
        testDocument.setContent(content);
    }

    @Test
    void testValidateDocumentWithMockMetadata() {
        // Arrange
        when(metadataService.validateMetadata(any(Map.class)))
            .thenReturn(true);

        // Act
        boolean isValid = validationService.validateDocument(testDocument);

        // Assert
        assertTrue(isValid);
        verify(metadataService).validateMetadata(any(Map.class));
    }

    @Test
    void testValidateDocumentWithSpyContentValidator() {
        // Arrange
        doReturn(true).when(contentValidator).validateContent(any(String.class));

        // Act
        boolean isValid = validationService.validateDocument(testDocument);

        // Assert
        assertTrue(isValid);
        verify(contentValidator).validateContent(any(String.class));
    }

    @Test
    void testValidateDocumentWithRealContentValidation() {
        // Arrange
        // Using the real implementation of contentValidator (spy)
        Map<String, Object> invalidContent = new HashMap<>();
        invalidContent.put("title", "");
        invalidContent.put("content", "");
        testDocument.setContent(invalidContent);

        // Act
        boolean isValid = validationService.validateDocument(testDocument);

        // Assert
        assertFalse(isValid);
        verify(contentValidator).validateContent(any(String.class));
    }

    @Test
    void testValidateDocumentWithMockAndSpyInteraction() {
        // Arrange
        when(metadataService.validateMetadata(any(Map.class)))
            .thenReturn(true);
        doReturn(true).when(contentValidator).validateContent(any(String.class));

        // Act
        boolean isValid = validationService.validateDocument(testDocument);

        // Assert
        assertTrue(isValid);
        verify(metadataService).validateMetadata(any(Map.class));
        verify(contentValidator).validateContent(any(String.class));
    }
} 