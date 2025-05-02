package ch.wiss.m294_doc_api.service;

import ch.wiss.m294_doc_api.GenericDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Concrete bean that Spring can instantiate and inject into
 * {@link DocumentValidationService}.
 */
@Service
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private final GenericDocumentRepository documentRepo;

    @Autowired
    public DocumentMetadataServiceImpl(GenericDocumentRepository documentRepo) {
        this.documentRepo = documentRepo;
    }

    /** Very simple example‐validation – adapt to your real rules. */
    @Override
    public boolean validateMetadata(Map<String, Object> metadata) {
        if (metadata == null) return false;
        // require a non-blank “title” field
        Object title = metadata.get("title");
        return title instanceof String && !((String) title).trim().isEmpty();
    }

    /** Deletes by id and returns true if something was actually removed. */
    @Override
    public boolean deleteById(String id) {
        if (id == null || id.isBlank()) return false;
        if (!documentRepo.existsById(id)) return false;
        documentRepo.deleteById(id);
        return true;
    }
}
