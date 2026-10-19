package me.aidenjee.taskmanager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

public class TaskManager {
    private final HashMap<UUID, Task> tasks = new HashMap<>();

    public void createTask(String title, LocalDate dueDate) {
        Task task = new Task(title, dueDate);
        tasks.put(task.getUUID(), task);
    }

    public void deleteTask(UUID id) {
        tasks.remove(id);
    }

    public int getTasksQuantity() {
        return tasks.size();
    }

    public Task getTaskByID(UUID id) {
        return tasks.get(id);
    }
}