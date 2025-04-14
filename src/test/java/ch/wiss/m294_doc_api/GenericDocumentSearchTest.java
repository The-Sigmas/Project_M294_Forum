package ch.wiss.m294_doc_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GenericDocumentSearchTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testAdvancedDocumentSearch() {
        String collectionName = "searchable_docs";
        
        // Create test documents with different structures
        createTestDocument("Programming Guide", 
            Arrays.asList("java", "spring"), 
            "technical",
            4.5);
            
        createTestDocument("Cooking Recipe", 
            Arrays.asList("food", "italian"), 
            "culinary",
            4.8);
            
        createTestDocument("Travel Blog", 
            Arrays.asList("travel", "europe"), 
            "lifestyle",
            4.2);

        // Search by tags
        Query tagQuery = new Query(Criteria.where("content.tags").in("java"));
        List<GenericDocument> techDocs = mongoTemplate.find(tagQuery, GenericDocument.class, collectionName);
        assertEquals(1, techDocs.size());
        assertEquals("Programming Guide", techDocs.get(0).getContent().get("title"));

        // Search by rating range
        Query ratingQuery = new Query(Criteria.where("content.rating").gte(4.5));
        List<GenericDocument> highRatedDocs = mongoTemplate.find(ratingQuery, GenericDocument.class, collectionName);
        assertEquals(2, highRatedDocs.size());

        // Complex query: category + minimum rating
        Query complexQuery = new Query(Criteria.where("content.category").is("technical")
            .and("content.rating").gte(4.0));
        List<GenericDocument> filteredDocs = mongoTemplate.find(complexQuery, GenericDocument.class, collectionName);
        assertEquals(1, filteredDocs.size());
        assertEquals("Programming Guide", filteredDocs.get(0).getContent().get("title"));
    }

    private void createTestDocument(String title, List<String> tags, String category, double rating) {
        GenericDocument doc = new GenericDocument();
        Map<String, Object> content = new HashMap<>();
        content.put("title", title);
        content.put("tags", tags);
        content.put("category", category);
        content.put("rating", rating);
        doc.setContent(content);
        mongoTemplate.save(doc, "searchable_docs");
    }
}
