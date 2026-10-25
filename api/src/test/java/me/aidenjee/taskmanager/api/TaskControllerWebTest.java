package me.aidenjee.taskmanager.api;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TaskControllerWebTest {

    /* GetTask() Tests */
    @Test 
    void getTaskNonExistentReturns404() throws Exception {
        TaskManager mockManager = mock(TaskManager.class);
        TaskController controller = new TaskController(mockManager);
        UUID id = UUID.randomUUID();
        when(mockManager.getTaskByID(id)).thenReturn(null);

        MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        mockMvc.perform(get("/tasks/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void getTaskReturnsCorrectTaskResponse() throws Exception {
        TaskManager mockManager = mock(TaskManager.class);
        TaskController controller = new TaskController(mockManager);
        Task task = new Task("Learn Spring Boot");
        UUID id = task.getUUID();
        when(mockManager.getTaskByID(id)).thenReturn(task);

        MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();
        
        mockMvc.perform(get("/tasks/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Learn Spring Boot"));
    }


    @Test
    void getTaskCountReturns200() throws Exception {
        TaskManager mockManager = mock(TaskManager.class);
        TaskController controller = new TaskController(mockManager);

        MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        when(mockManager.getTaskCount()).thenReturn(5);

        mockMvc.perform(get("/tasks/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
    }
    
}
