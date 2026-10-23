package me.aidenjee.taskmanager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskManagerTest {
    private TaskManager taskManager;
    private HashMap<String, UUID> taskIds;
    private List<TaskData> tasks;
    record TaskData(String title, LocalDate dueDate) {}


    @BeforeEach 
    public void setUp() {
        taskManager = new TaskManager();
        taskIds = new HashMap<>();
        // do not add duplicate in before each, it will break test cases
        tasks = List.of(
            new TaskData("Overdue", LocalDate.MAX),
            new TaskData("Not Overdue", LocalDate.MIN),
            new TaskData("Now", LocalDate.now()),
            new TaskData("Aiden's Task", LocalDate.MIN),
            new TaskData("Test Data", LocalDate.MIN)
        );
        tasks.stream().forEach(data -> {
            UUID uuid = taskManager.createTask(data.title(), data.dueDate());
            taskIds.put(data.title(), uuid);
        });
    }

    @Test 
    public void testGetIncompleteTasksShouldReturnAllTasks() {
        // all task should be incomplete
        List<Task> incompleteTasks = taskManager.getIncompleteTasks();

        Set<String> expectedTitles = tasks.stream()
            .map(task -> task.title)
            .collect(Collectors.toSet());
        Set<String> actualTitles = incompleteTasks.stream()
            .map(task -> task.getTitle())
            .collect(Collectors.toSet());

        assertEquals(taskManager.getTaskCount(), incompleteTasks.size());
        assertEquals(expectedTitles, actualTitles);
    }

    @Test
    public void testGetIncompleteTasksShouldRemoveCompleteTask() {
        // mark complete task
        Task aidenTask = taskManager.getTaskByID(taskIds.get("Aiden's Task"));
        aidenTask.editCompletedStatus(true);

        List<Task> incompleteTasks = taskManager.getIncompleteTasks();
        Set<String> expectedTitles = tasks.stream()
            .map(task -> task.title)
            .filter(title -> !title.equals("Aiden's Task"))
            .collect(Collectors.toSet());
        Set<String> actualTitles = incompleteTasks.stream()
            .map(Task::getTitle)
            .collect(Collectors.toSet());

        assertEquals(taskManager.getTaskCount() - 1, incompleteTasks.size());
        assertEquals(expectedTitles, actualTitles);
    }

    @Test 
    public void concurrentInsertions() throws InterruptedException {
        Task task = new Task("concurrency test");
        TaskManager testTaskManager = new TaskManager();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> testTaskManager.addTaskIfAbsent(task));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
                throw e;
        }
        assertEquals(1, testTaskManager.getTaskCount());
    }
}
