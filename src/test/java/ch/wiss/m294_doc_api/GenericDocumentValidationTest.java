package ch.wiss.m294_doc_api;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDocumentValidationTest {
    
    private Validator validator;
    private GenericDocument document;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        document = new GenericDocument();
    }

    @Test
    void testContentValidation_ComplexNestedStructure() {
        // Testing with a complex nested structure
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> author = new HashMap<>();
        author.put("name", "John Doe");
        author.put("email", "john@example.com");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("created", "2025-04-14");
        metadata.put("tags", new String[]{"important", "urgent"});
        
        content.put("title", "Test Document");
        content.put("author", author);
        content.put("metadata", metadata);
        content.put("version", 2.0);
        
        document.setContent(content);
        
        Set<ConstraintViolation<GenericDocument>> violations = validator.validate(document);
        assertTrue(violations.isEmpty());
        assertEquals(2.0, document.getContent().get("version"));
        @SuppressWarnings("unchecked")
        Map<String, Object> authorMap = (Map<String, Object>) document.getContent().get("author");
        assertEquals("John Doe", authorMap.get("name"));
    }
}
