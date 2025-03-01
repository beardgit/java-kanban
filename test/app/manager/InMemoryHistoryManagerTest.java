package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
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

    @Test
    public void testHistoryLimit() {
        HistoryManager historyManager1 = new InMemoryHistoryManager();
        HistoryManager historyManager2 = new InMemoryHistoryManager();

        for (int i = 0; i < 15; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            Task epic = new Task("Epic " + i, "Description " + i);
            historyManager1.addToHistory(task);
            historyManager2.addToHistory(epic);

        }
        List<Task> history1 = historyManager1.getHistory();
        assertEquals(10, history1.size()); // История должна содержать ровно 10 элементов
        assertEquals("Task 14", history1.get(9).getName()); // Последняя добавленная задача
        assertEquals("Task 5", history1.get(0).getName()); // Самая старая задача после удаления


        List<Task> history2 = historyManager2.getHistory();
        assertEquals(10, history2.size()); // История должна содержать ровно 10 элементов
        assertEquals("Epic 14", history2.get(9).getName()); // Последняя добавленная задача
        assertEquals("Epic 5", history2.get(0).getName()); // Самая старая задача после у

    }
}