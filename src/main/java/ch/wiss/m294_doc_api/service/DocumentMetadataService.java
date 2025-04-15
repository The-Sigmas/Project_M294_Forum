package ch.wiss.m294_doc_api.service;

import java.util.Map;

public interface DocumentMetadataService {
    boolean validateMetadata(Map<String, Object> metadata);
} 