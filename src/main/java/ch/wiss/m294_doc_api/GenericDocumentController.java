package ch.wiss.m294_doc_api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{collectionName}")
public class GenericDocumentController {

    @Autowired
    private GenericDocumentService genericDocumentService;

    /* ---------- CRUD ---------- */

    @PostMapping
    public ResponseEntity<GenericDocument> createDocument(@PathVariable String collectionName,
                                                          @RequestBody GenericDocument document) {
        GenericDocument savedDoc = genericDocumentService.save(collectionName, document);
        return ResponseEntity.ok(savedDoc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable String collectionName,
                                            @PathVariable String id,
                                            @RequestBody GenericDocument document) {
        if (document.getId() == null || !document.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Document ID in body must match the ID in the URL.");
        }
        return genericDocumentService.updateById(collectionName, id, document)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<GenericDocument> getAllDocuments(@PathVariable String collectionName) {
        return genericDocumentService.findAll(collectionName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericDocument> getDocumentById(@PathVariable String collectionName,
                                                           @PathVariable String id) {
        return genericDocumentService.findById(collectionName, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String collectionName,
                                               @PathVariable String id) {
        boolean deleted = genericDocumentService.deleteById(collectionName, id);
        return deleted ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }

    /* ---------- UPVOTE ---------- */

    @PutMapping("/{id}/upvote")
    public ResponseEntity<GenericDocument> upvoteDocument(@PathVariable String collectionName,
                                                          @PathVariable String id) {
        Optional<GenericDocument> opt = genericDocumentService.findById(collectionName, id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GenericDocument doc = opt.get();
        Map<String, Object> content = doc.getContent();

        int votes = 0;
        Object vObj = content.get("votes");
        if (vObj instanceof Integer)       votes = (Integer) vObj;
        else if (vObj instanceof Number)   votes = ((Number) vObj).intValue();

        content.put("votes", votes + 1);
        doc.setContent(content);
        genericDocumentService.save(collectionName, doc);

        return ResponseEntity.ok(doc);
    }
}
