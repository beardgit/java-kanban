package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndGetTask() {
        Task task = taskManager.appendTask(new Task("Task Name", "Description"));
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача не найдена");
    }

    @Test
    void testAddAndGetEpic() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик не найден");
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        Subtask subtask = taskManager.appendSubtask(new Subtask("Subtask Name", "Description", epic));
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача не найдена");
    }


    @Test
    void testAddAndGetTasks() {
        Task task = taskManager.appendTask(new Task("Task Name", "Description"));
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        Subtask subtask = taskManager.appendSubtask(new Subtask("Subtask Name", "Description", (Epic) epic));

        assertNotNull(taskManager.getTaskById(task.getId()), "Задача не найдена");
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик не найден");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача не найдена");
    }


}

