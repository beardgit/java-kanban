package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldAddTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task = new Task("Task 1", "Description 1");

        manager.appendTask(task);

        assertNotNull(manager.getTaskById(task.getId()));
        assertEquals(1, manager.getAllTasks().size());
    }



    @Test
    void shouldUpdateTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task = new Task("Task 1", "Description 1");
        manager.appendTask(task);

        Task updatedTask = new Task("Updated Task", "Updated Description");
        updatedTask.setId(task.getId());
        manager.updateTask(updatedTask);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals("Updated Task", retrievedTask.getName());
    }

    @Test
    void shouldClearAllTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        manager.appendTask(new Task("Task 1", "Description 1"));
        manager.appendTask(new Task("Task 2", "Description 2"));

        boolean isCleared = manager.clearTasks();
        assertTrue(isCleared);
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void shouldAddEpicAndSubtask() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic("Epic 1", "Description 1");
        manager.appendEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic);
        manager.appendSubtask(subtask);

        assertNotNull(manager.getEpicById(epic.getId()));
        assertNotNull(manager.getSubtaskById(subtask.getId()));
        assertEquals(1, epic.getListSubtasks().size());
    }


    @Test
    void shouldGetHistory() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.appendTask(task1);
        manager.appendTask(task2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }



}

