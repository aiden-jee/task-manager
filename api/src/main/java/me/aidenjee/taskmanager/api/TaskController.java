package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@RestController 
public class TaskController {
    private final TaskManager taskManager;

    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    // requests
    public record CreateTaskRequest(String title, LocalDate dueDate) {}

    // responses
    public record GetTaskResponse(String title, LocalDate dueDate, boolean completed) {}

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreateTaskRequest req) {
        UUID taskUUID = taskManager.createTask(req.title(), req.dueDate());
        Task task = taskManager.getTaskByID(taskUUID);
        return task;
    }

    @GetMapping("/tasks/{id}")
    public GetTaskResponse getTask(@PathVariable("id") UUID uuid) {
        Task task = taskManager.getTaskByID(uuid);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!");
        }
        GetTaskResponse res = new GetTaskResponse(
            task.getTitle(), 
            task.getDueDate(), 
            task.getCompletedStatus()
        );
        return res;
    }
    
    

    @GetMapping("/tasks/count")
    public int getTaskCount() {
        return taskManager.getTaskCount();
    }
}
