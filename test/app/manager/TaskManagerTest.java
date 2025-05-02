package app.manager;

import app.enumeration.StatusTasks;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void init() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Task 1", "Desc");
        taskManager.appendTask(task);
        Task result = taskManager.getTaskById(task.getId());
        assertEquals(task.getName(), result.getName());
    }

    @Test
    void testEpicStatusAllNew() {
        Epic epic = taskManager.appendEpic(new Epic("Epic", "Desc"));
        taskManager.appendSubtask(new Subtask("S1", "D", epic.getId()));
        taskManager.appendSubtask(new Subtask("S2", "D", epic.getId()));
        assertEquals(StatusTasks.NEW, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void testEpicStatusAllDone() {
        Epic epic = taskManager.appendEpic(new Epic("Epic", "Desc"));
        Subtask s1 = new Subtask("S1", "D", epic.getId());
        Subtask s2 = new Subtask("S2", "D", epic.getId());
        s1.setStatus(StatusTasks.DONE);
        s2.setStatus(StatusTasks.DONE);
        taskManager.appendSubtask(s1);
        taskManager.appendSubtask(s2);
        assertEquals(StatusTasks.DONE, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void testEpicStatusMixed() {
        Epic epic = taskManager.appendEpic(new Epic("Epic", "Desc"));
        Subtask s1 = new Subtask("S1", "D", epic.getId());
        Subtask s2 = new Subtask("S2", "D", epic.getId());
        s2.setStatus(StatusTasks.DONE);
        taskManager.appendSubtask(s1);
        taskManager.appendSubtask(s2);
        assertEquals(StatusTasks.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void testEpicStatusInProgress() {
        Epic epic = taskManager.appendEpic(new Epic("Epic", "Desc"));
        Subtask s1 = new Subtask("S1", "D", epic.getId());
        Subtask s2 = new Subtask("S2", "D", epic.getId());
        s1.setStatus(StatusTasks.IN_PROGRESS);
        s2.setStatus(StatusTasks.IN_PROGRESS);
        taskManager.appendSubtask(s1);
        taskManager.appendSubtask(s2);
        assertEquals(StatusTasks.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void testSubtaskLinkedToEpic() {
        Epic epic = taskManager.appendEpic(new Epic("Epic", "Desc"));
        Subtask sub = taskManager.appendSubtask(new Subtask("Sub", "D", epic.getId()));
        assertTrue(taskManager.getEpicById(epic.getId()).getListSubtasks().contains(sub));
    }

    @Test
    void testHistoryAfterAccess() {
        Task task = taskManager.appendTask(new Task("T", "D"));
        taskManager.getTaskById(task.getId());
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void testNoIntersectionAllowed() {
        Task task1 = new Task("T1", "D", Instant.parse("2023-10-10T10:00:00Z"), Duration.ofMinutes(60));
        Task task2 = new Task("T2", "D", Instant.parse("2023-10-10T10:30:00Z"), Duration.ofMinutes(30));
        taskManager.appendTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.appendTask(task2));
    }

    @Test
    void testPrioritizedTaskOrder() {
        Task t1 = new Task("T1", "D", Instant.parse("2023-10-10T10:00:00Z"), Duration.ofMinutes(60));
        Task t2 = new Task("T2", "D", Instant.parse("2023-10-10T12:00:00Z"), Duration.ofMinutes(30));
        taskManager.appendTask(t2);
        taskManager.appendTask(t1);
        List<Task> sorted = taskManager.getPrioritizedTasks();
        assertEquals(t1, sorted.get(0));
        assertEquals(t2, sorted.get(1));
    }
}