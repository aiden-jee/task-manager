package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;



@RestController 
public class TaskController {
    private final TaskManager taskManager;

    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    // requests
    public record CreateTaskRequest(@NotBlank String title, LocalDate dueDate) {}
    public record PatchTaskRequest(String title, LocalDate dueDate, Boolean completed) {}

    // responses
    public record TaskResponse(String title, LocalDate dueDate, boolean completed) {}
    public record CreateTaskResponse(UUID id, String title, LocalDate dueDate, boolean completed) {}


    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTaskResponse createTask(@Valid @RequestBody CreateTaskRequest req) {
        UUID taskUUID = taskManager.createTask(req.title(), req.dueDate());
        Task task = taskManager.getTaskByID(taskUUID);
        return new CreateTaskResponse(taskUUID, task.getTitle(), task.getDueDate(), task.getCompletedStatus());
    }

    @GetMapping("/tasks")
    public List<TaskResponse> getTasks() {
        return taskManager.getTasks().stream()
            .map(task -> new TaskResponse(task.getTitle(), task.getDueDate(), task.getCompletedStatus()))
            .toList();
    }

    @GetMapping("/tasks/{id}")
    public TaskResponse getTask(@PathVariable("id") UUID uuid) {
        Task task = taskManager.getTaskByID(uuid);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!");
        }
        TaskResponse res = new TaskResponse(
            task.getTitle(), 
            task.getDueDate(), 
            task.getCompletedStatus()
        );
        return res;
    }
    
    @PatchMapping("/tasks/{id}")
    public TaskResponse patchTask(@PathVariable UUID id, @RequestBody PatchTaskRequest req) {     
        Task task = taskManager.patchTask(id, req.title(), req.dueDate(), req.completed());
        return new TaskResponse(task.getTitle(), task.getDueDate(), task.getCompletedStatus());

    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable UUID id) {
        taskManager.deleteTask(id);

    }

    @GetMapping("/tasks/count")
    public int getTaskCount() {
        return taskManager.getTaskCount();
    }

}
