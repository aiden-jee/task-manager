package me.aidenjee.taskmanager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TaskTest {
    @ParameterizedTest
    @CsvSource({
        "Study Java, 2026-10-20",
        "Learn Spring Boot, 2026-11-01",
        "Prepare for interviews, 2026-12-15"
    })
    void constructorShouldInitializeFields(String title, LocalDate dueDate) {
        Task task = new Task(title, dueDate);
        assertAll(
            () -> assertEquals(title, task.getTitle()),
            () -> assertEquals(dueDate, task.getDueDate()),
            () -> assertFalse(task.getCompletedStatus()),
            () -> assertNotNull(task.getUUID())
        );
    }

    @Test 
    void editCompletedStatusAltersField() {
        Task task = new Task("test", LocalDate.now());
        assertFalse(task.getCompletedStatus());
        task.editCompletedStatus(true);
        assertTrue(task.getCompletedStatus());
    }

    @Test
    void constructorShouldGenerateUniqueIds() {
        String title = "Study Java";
        LocalDate dueDate = LocalDate.of(2026, 10, 20);

        Task task1 = new Task(title, dueDate);
        Task task2 = new Task(title, dueDate);

        assertNotEquals(task1.getUUID(), task2.getUUID());
    }

    @Test 
    void overdueFalseForCompletedTasks() {
        Task task = new Task("test", LocalDate.of(1900, 10, 10));
        assertTrue(task.getOverdue());
        task.editCompletedStatus(true);
        assertFalse(task.getOverdue());
    }
}