package app.manager;

import app.tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskIntersectiontest {
    @Test
    public void shouldThrowExceptionOnTimeIntersection() {
        TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

        Task task1 = new Task("Task 1", "Description", Instant.parse("2023-10-01T10:00:00Z"), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description", Instant.parse("2023-10-01T10:15:00Z"), Duration.ofMinutes(30));

        taskManager.appendTask(task1);

        assertThrows(IllegalArgumentException.class, () -> taskManager.appendTask(task2));
    }

    @Test
    public void shouldNotThrowExceptionOnNonIntersectingTasks() {
        TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

        Task task1 = new Task("Task 1", "Description", Instant.parse("2023-10-01T10:00:00Z"), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description", Instant.parse("2023-10-01T11:00:00Z"), Duration.ofMinutes(30));

        taskManager.appendTask(task1);
        taskManager.appendTask(task2); // Должно работать без исключений
    }
}
