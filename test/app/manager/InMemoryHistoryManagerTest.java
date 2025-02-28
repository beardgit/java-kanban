package app.manager;

import app.tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void testAddToHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task Name", "Description");

        historyManager.addToHistory(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertFalse(history.isEmpty(), "История должна содержать элементы");
        assertEquals(1, history.size(), "Неверное количество задач в истории");
        assertEquals(task, history.get(0), "Задачи в истории не совпадают");
    }
}