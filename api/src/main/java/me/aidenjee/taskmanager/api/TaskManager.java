package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TaskManager {
    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();

    public UUID createTask(String title, LocalDate dueDate) {
        Task task;
        if (dueDate == null) {
            task = new Task(title);
        }
        else {
            task = new Task(title, dueDate);
        }
        UUID uuid = task.getUUID();
        tasks.put(uuid, task);
        return uuid;
    }

    public boolean addTaskIfAbsent(Task task) {
        return tasks.putIfAbsent(task.getUUID(), task) == null;
    }

    public void deleteTask(UUID id) {
        Task task = tasks.remove(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found!");
        }
    }

    public int getTaskCount() {
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

    public List<Task> getTasks() {
        return tasks.values().stream().toList();
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

    public Task patchTask(UUID id, String title, LocalDate dueDate, Boolean completed) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found");
        }
        if (title != null) {
            task.editTitle(title);
        }
        if (dueDate != null) {
            task.editDueDate(dueDate);
        }
        if (completed != null) {
            task.editCompletedStatus(completed);
        } 
        return task;       
    }
}