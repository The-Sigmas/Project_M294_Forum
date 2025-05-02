package ch.wiss.m294_doc_api.config;

import ch.wiss.m294_doc_api.service.DocumentMetadataService;
import ch.wiss.m294_doc_api.service.DocumentMetadataServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class ServiceConfig {
    
    @Bean
    public DocumentMetadataService documentMetadataService(MongoTemplate mongoTemplate) {
        DocumentMetadataServiceImpl service = new DocumentMetadataServiceImpl();
        service.setMongoTemplate(mongoTemplate);
        return service;
    }
} 