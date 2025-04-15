package ch.wiss.m294_doc_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenericDocumentController.class)
public class GenericDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenericDocumentService documentService;

    @Test
    void testCreateDocument_ValidatesJsonStructure() throws Exception {
        // Test with complex JSON structure
        String jsonContent = """
            {
                "content": {
                    "title": "Test Document",
                    "metadata": {
                        "author": "John Doe",
                        "created": "2025-04-14",
                        "tags": ["test", "validation"]
                    },
                    "sections": [
                        {
                            "heading": "Introduction",
                            "content": "This is the introduction"
                        },
                        {
                            "heading": "Conclusion",
                            "content": "This is the conclusion"
                        }
                    ]
                }
            }
            """;

        GenericDocument mockResponse = new GenericDocument();
        Map<String, Object> responseContent = new HashMap<>();
        String docId = "test123";
        mockResponse.setId(docId);
        responseContent.put("id", docId);
        responseContent.put("title", "Test Document");
        mockResponse.setContent(responseContent);

        when(documentService.save(eq("documents"), any(GenericDocument.class)))
            .thenReturn(mockResponse);

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test123"))
                .andExpect(header().exists("Location"));
    }
}
