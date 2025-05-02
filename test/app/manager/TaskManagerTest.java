package app.manager;

import app.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    // Общие тесты для всех реализаций TaskManager
    @Test
    public void testGetAllTasks() {
        Task task = new Task("Task 1", "Description");
        taskManager.appendTask(task);
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void testClearTasks() {
        Task task = new Task("Task 1", "Description");
        taskManager.appendTask(task);
        taskManager.clearTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }
}