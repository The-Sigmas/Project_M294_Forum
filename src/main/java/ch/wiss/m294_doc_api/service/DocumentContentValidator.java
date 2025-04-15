package ch.wiss.m294_doc_api.service;

import org.springframework.stereotype.Component;

@Component
public class DocumentContentValidator {
    
    public boolean validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        return content.length() >= 10; // Example validation: content must be at least 10 characters
    }
} 