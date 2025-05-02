package ch.wiss.m294_doc_api.service;

import java.util.Map;
import ch.wiss.m294_doc_api.model.DocumentMetadata;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean validateMetadata(Map<String, Object> metadata) {
        if (metadata == null) {
            return false;
        }
        
        // Check if required fields are present and not empty
        String title = (String) metadata.get("title");
        return title != null && !title.trim().isEmpty();
    }

    @Override
    public boolean deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, DocumentMetadata.class).getDeletedCount() > 0;
    }
} 