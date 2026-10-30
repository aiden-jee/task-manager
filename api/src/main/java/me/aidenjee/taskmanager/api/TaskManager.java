package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskManager {
    private final TaskRepository tasks;

    public TaskManager(TaskRepository tasks) {
        this.tasks = tasks;
    }

    public UUID createTask(String title, LocalDate dueDate) {
        Task task;
        if (dueDate == null) {
            task = new Task(title);
        }
        else {
            task = new Task(title, dueDate);
        }
        UUID uuid = task.getUUID();
        tasks.save(task);
        return uuid;
    }

    // public boolean addTaskIfAbsent(Task task) {
    //     if (tasks.exists(task)) {
    //         return false;
    //     }
    //     tasks.saveAndFlush(task);
    //     return true;
    // }

    public void deleteTask(UUID id) {
        if (tasks.existsById(id)) {
            tasks.deleteById(id);
        }
        else {
            throw new NoSuchElementException("Task does not exist!");
        }
    }

    public long getTaskCount() {
        return tasks.count();
    }

    public Task getTaskByID(UUID id) {
        Task task = tasks.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Task does not exist!"));
        return task;
    }

    public List<Task> getIncompleteTasks() {
        return tasks.findAll()
            .stream()
            .filter(task -> !task.getCompletedStatus())
            .toList();
    }

    public List<Task> getTasks() {
        return tasks.findAll();
    }

    public List<Task> getTasksSortedByTitle() {
        return tasks.findAll(Sort.by("title"));
    }

    public long countCompletedTasks() {
        return tasks.findAll().stream()
            .filter(task -> task.getCompletedStatus())
            .count();
    }

    public Map<Boolean, List<Task>> groupTasksByCompletion() {
        return tasks.findAll().stream()
            .collect(Collectors.partitioningBy(task -> task.getCompletedStatus()));
    }

    @Transactional
    public Task patchTask(UUID id, String title, LocalDate dueDate, Boolean completed) {
        Task task = tasks.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Task does not exist!"));
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