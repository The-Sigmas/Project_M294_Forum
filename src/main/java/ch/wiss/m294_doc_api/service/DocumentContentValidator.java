package ch.wiss.m294_doc_api.service;

import org.springframework.stereotype.Component;

@Component
public class DocumentContentValidator {
    
    public boolean validateContent(String content) {
        return content != null && !content.trim().isEmpty();
    }
} 