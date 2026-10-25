package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController 
public class TaskController {
    private final TaskManager taskManager;

    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public record CreateTaskRequest(String title, LocalDate dueDate) {}

    @PostMapping("/tasks")
    public Task createTask(@RequestBody CreateTaskRequest req) {
        UUID taskUUID = taskManager.createTask(req.title(), req.dueDate());
        Task task = taskManager.getTaskByID(taskUUID);
        return task;
    }
    

    @GetMapping("/tasks/count")
    public int getTaskCount() {
        return taskManager.getTaskCount();
    }
}
