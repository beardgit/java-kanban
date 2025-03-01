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
    void testDeleteTask() {
        Task task = taskManager.appendTask(new Task("Task Name", "Description"));
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTaskById(task.getId()), "Задача должна быть удалена");
    }

    @Test
    void testDeleteEpic() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpicById(epic.getId()), "Эпик должен быть удален");
    }

    @Test
    void testDeleteSubtask() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        Subtask subtask = taskManager.appendSubtask(new Subtask("Subtask Name", "Description", epic));
        taskManager.deleteSubtask(subtask.getId());
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача должна быть удалена");
    }

    @Test
    void testUpdateTask() {
        Task task = taskManager.appendTask(new Task("Task Name", "Description"));
        Task updatedTask = new Task(task.getId(), "Updated Task Name", "Updated Description");
        taskManager.updateTask(updatedTask);
        assertEquals("Updated Task Name", taskManager.getTaskById(task.getId()).getName(), "Имя задачи должно быть обновлено");
        assertEquals("Updated Description", taskManager.getTaskById(task.getId()).getDescription(), "Описание задачи должно быть обновлено");
    }

    @Test
    void testUpdateEpic() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        Epic updatedEpic = new Epic(epic.getId(), "Updated Epic Name", "Updated Description");
        taskManager.updateEpic(updatedEpic);
        assertEquals("Updated Epic Name", taskManager.getEpicById(epic.getId()).getName(), "Имя эпика должно быть обновлено");
        assertEquals("Updated Description", taskManager.getEpicById(epic.getId()).getDescription(), "Описание эпика должно быть обновлено");
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = taskManager.appendEpic(new Epic("Epic Name", "Description"));
        Subtask subtask = taskManager.appendSubtask(new Subtask("Subtask Name", "Description", epic));
        Subtask updatedSubtask = new Subtask(subtask.getId(), "Updated Subtask Name", "Updated Description", epic);
        taskManager.updateSubtask(updatedSubtask);
        assertEquals("Updated Subtask Name", taskManager.getSubtaskById(subtask.getId()).getName(), "Имя подзадачи должно быть обновлено");
        assertEquals("Updated Description", taskManager.getSubtaskById(subtask.getId()).getDescription(), "Описание подзадачи должно быть обновлено");
    }


}
