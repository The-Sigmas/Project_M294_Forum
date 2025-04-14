package ch.wiss.m294_doc_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.database=testdb",
    "spring.data.mongodb.port=27017"
})
public class GenericDocumentServiceCRUDTest {

    @Autowired
    private GenericDocumentService documentService;

    @Test
    void testDocumentVersioning() {
        String collectionName = "versioned_docs";
        
        // Create initial version
        GenericDocument doc = new GenericDocument();
        Map<String, Object> content = new HashMap<>();
        content.put("version", 1);
        content.put("content", "Initial version");
        doc.setContent(content);
        
        GenericDocument saved = documentService.save(collectionName, doc);
        String docId = saved.getId();
        
        // Update with new version
        Map<String, Object> updatedContent = new HashMap<>();
        updatedContent.put("version", 2);
        updatedContent.put("content", "Updated version");
        updatedContent.put("lastVersion", content);
        
        GenericDocument updatedDoc = new GenericDocument();
        updatedDoc.setContent(updatedContent);
        
        Optional<GenericDocument> updated = documentService.updateById(collectionName, docId, updatedDoc);
        
        assertTrue(updated.isPresent());
        assertEquals(2, updated.get().getContent().get("version"));
        assertNotNull(updated.get().getContent().get("lastVersion"));
    }
}
