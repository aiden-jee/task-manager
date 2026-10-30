package me.aidenjee.taskmanager.api;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity 
@Table(name="tasks")
public class Task {
    
    @Id 
    @Column(nullable=false)
    private UUID id;

    @Column(nullable=false)
    private String title;

    @Column(name="DUE_DATE")
    private LocalDate dueDate;

    @Column(name="COMPLETED_STATUS")
    private boolean completed; 

    @Version
    @Column
    private Long version;

    protected Task() {}

    Task(String title) {
        this.title = title;
        this.dueDate = null;
        completed = false;
        this.id = UUID.randomUUID();
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

    public Long getVersion() {
        return version;
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