package app.manager;

import app.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {


    @Test
    void testAddTaskToHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description 1");
        task.setId(1);

        historyManager.addToHistory(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void testRemoveTaskFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description 1");
        task.setId(1);

        historyManager.addToHistory(task);
        historyManager.removeFromHistory(task);

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }


    @Test
    void testNotAllowDuplicateTasksInHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description 1");
        task.setId(1);

        historyManager.addToHistory(task);
        historyManager.addToHistory(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void testMaintainFIFOOrderInHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        task1.setId(1);
        task2.setId(2);

        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testMoveTaskToEndOfHistoryOnReAddition() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        task1.setId(1);
        task2.setId(2);

        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task1); // Повторное добавление

        List<Task> history = historyManager.getHistory();
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }


}