package me.aidenjee.taskmanager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Task> getIncompleteTasks() {
        return tasks.values()
            .stream()
            .filter(task -> !task.getCompletedStatus())
            .toList();
    }

    public List<Task> getTasksSortedByTitle() {
        return tasks.values().stream()
            .sorted(Comparator.comparing(Task::getTitle))
            .toList();
    }

    public long countCompletedTasks() {
        return tasks.values().stream()
            .filter(task -> task.getCompletedStatus())
            .count();
    }

    public Map<Boolean, List<Task>> groupTasksByCompletion() {
        return tasks.values().stream()
            .collect(Collectors.partitioningBy(task -> task.getCompletedStatus()));
    }
}