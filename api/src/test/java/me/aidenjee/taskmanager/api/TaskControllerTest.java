package me.aidenjee.taskmanager.api;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import me.aidenjee.taskmanager.api.TaskController.GetTaskResponse;

class TaskControllerTest {

    @Test
    void getTaskReturnsCorrectResponse() {
        TaskManager mockManager = mock(TaskManager.class);
        TaskController controller =
                new TaskController(mockManager);

        UUID id = UUID.randomUUID();
        Task task = new Task("Study Spring Boot");

        when(mockManager.getTaskByID(id)).thenReturn(task);

        GetTaskResponse response = controller.getTask(id);

        assertEquals("Study Spring Boot", response.title());
        assertEquals(task.getDueDate(), response.dueDate());
        assertEquals(task.getCompletedStatus(), response.completed());
    }

    @Test 
    void getTaskReturnsCodeNotFound() {
        TaskManager mockManager = mock(TaskManager.class);
        TaskController controller = new TaskController(mockManager);
        UUID id = UUID.randomUUID();
        when(mockManager.getTaskByID(id)).thenReturn(null);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> controller.getTask(id)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
  