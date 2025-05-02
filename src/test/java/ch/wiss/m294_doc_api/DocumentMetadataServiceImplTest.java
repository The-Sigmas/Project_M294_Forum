package ch.wiss.m294_doc_api;

import ch.wiss.m294_doc_api.model.DocumentMetadata;
import ch.wiss.m294_doc_api.service.DocumentMetadataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.mongodb.client.result.DeleteResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class DocumentMetadataServiceImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private DocumentMetadataServiceImpl metadataService;

    @Test
    void testDeleteByIdExisting() {
        // Arrange
        String id = "123";
        when(mongoTemplate.remove(any(Query.class), eq(DocumentMetadata.class)))
            .thenReturn(DeleteResult.acknowledged(1));

        // Act
        boolean result = metadataService.deleteById(id);

        // Assert
        assertTrue(result);
        verify(mongoTemplate).remove(any(Query.class), eq(DocumentMetadata.class));
    }
} 