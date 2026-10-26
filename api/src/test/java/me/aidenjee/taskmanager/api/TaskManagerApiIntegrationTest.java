package me.aidenjee.taskmanager.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskManagerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc; 

    @Test
    void createAndRetrieveTask() throws Exception {
        final String jsonRes  = mockMvc.perform(post("/tasks")
             .contentType(MediaType.APPLICATION_JSON)
             .content("""
                     { "title": "Spring Boot Test" }
                     """))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        String id = JsonPath.read(jsonRes, "$.id");
        
        final String taskRes = mockMvc.perform(get("/tasks/" + id))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        assertEquals("Spring Boot Test", JsonPath.read(taskRes, "$.title"));
    }
}
