package ch.wiss.m294_doc_api.service;

import ch.wiss.m294_doc_api.GenericDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentValidationService {

    private final DocumentMetadataService metadataService;
    private final DocumentContentValidator contentValidator;

    @Autowired
    public DocumentValidationService(DocumentMetadataService metadataService, 
                                   DocumentContentValidator contentValidator) {
        this.metadataService = metadataService;
        this.contentValidator = contentValidator;
    }

    public boolean validateDocument(GenericDocument document) {
        if (document == null || document.getContent() == null) {
            return false;
        }

        // Validate metadata
        boolean isMetadataValid = metadataService.validateMetadata(document.getContent());
        if (!isMetadataValid) {
            return false;
        }

        // Validate content
        String content = (String) document.getContent().get("content");
        return contentValidator.validateContent(content);
    }
} 