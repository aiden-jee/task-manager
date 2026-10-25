package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private final UUID id = UUID.randomUUID();
    private String title;
    private LocalDate dueDate;
    private boolean completed; 

    Task(String title) {
        this.title = title;
        this.dueDate = LocalDate.MAX;
        completed = false;
    }

    Task(String title, LocalDate dueDate) {
        this(title);
        this.dueDate = dueDate;
    }

    public UUID getUUID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean getCompletedStatus() {
        return completed;
    }

    public boolean getOverdue() {
        if (completed) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public void editTitle(String title) {
        this.title = title;
    }

    public void editDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void editCompletedStatus(boolean completed) {
        this.completed = completed;
    }
}