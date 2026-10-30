package me.aidenjee.taskmanager.api;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveAndRetrieveTask() {
        Task task = new Task("Task Repository Test Task");
        taskRepository.save(task);
        UUID id = task.getUUID();

        entityManager.flush();
        entityManager.clear();

        Task repoTask = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        assertEquals("Task Repository Test Task", repoTask.getTitle());
        assertEquals(id, repoTask.getUUID());
    }
}