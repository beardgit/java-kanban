package app.manager;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class ManagersTest {

    @Test
    void testManagersReturnsInitializedInstances() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Assertions.assertNotNull(taskManager, "TaskManager не должен быть null");
        Assertions.assertNotNull(historyManager, "HistoryManager не должен быть null");

        Assertions.assertTrue(taskManager instanceof InMemoryTaskManager, "TaskManager должен быть экземпляром InMemoryTaskManager");
        Assertions.assertTrue(historyManager instanceof InMemoryHistoryManager, "HistoryManager должен быть экземпляром InMemoryHistoryManager");
    }
}