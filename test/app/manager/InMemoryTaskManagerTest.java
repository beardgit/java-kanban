package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        // Инициализация менеджера задач перед каждым тестом
        taskManager = createTaskManager();
    }

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    void testAddEpicAndSubtasks() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);

        // Проверяем, что эпик добавлен
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик не был добавлен.");

        // Создаем подзадачи и добавляем их
        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", epic.getId());
        taskManager.appendSubtask(subtask1);
        taskManager.appendSubtask(subtask2);

        // Проверяем, что подзадачи добавлены
        assertNotNull(taskManager.getSubtaskById(subtask1.getId()), "Подзадача 1 не была добавлена.");
        assertNotNull(taskManager.getSubtaskById(subtask2.getId()), "Подзадача 2 не была добавлена.");

        // Проверяем, что подзадачи добавлены в список подзадач эпика
        assertEquals(2, taskManager.getEpicById(epic.getId()).getListSubtasks().size(),
                "Подзадачи не были добавлены в список подзадач эпика.");
    }

    @Test
    void testDeleteEpicWithSubtasks() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);

        // Создаем подзадачи и добавляем их
        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", epic.getId());
        taskManager.appendSubtask(subtask1);
        taskManager.appendSubtask(subtask2);

        // Удаляем эпик
        taskManager.deleteEpic(epic.getId());

        // Проверяем, что список подзадач эпика пуст
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст после удаления эпика.");
    }

    @Test
    void testDeleteSubtask() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);

        // Создаем подзадачу и добавляем её
        Subtask subtask = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        taskManager.appendSubtask(subtask);

        // Удаляем подзадачу
        taskManager.deleteSubtask(subtask.getId());

        // Проверяем, что подзадача удалена из хранилища
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача не была удалена из хранилища.");

        // Проверяем, что подзадача удалена из списка подзадач эпика
        assertTrue(taskManager.getEpicById(epic.getId()).getListSubtasks().isEmpty(),
                "Подзадача не была удалена из списка подзадач эпика.");
    }

    @Test
    void testClearEpics() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);

        // Создаем подзадачи и добавляем их
        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", epic.getId());
        taskManager.appendSubtask(subtask1);
        taskManager.appendSubtask(subtask2);

        // Очищаем все эпики
        taskManager.clearEpics();

        // Проверяем, что все эпики удалены
        assertTrue(taskManager.getAllEpic().isEmpty(), "Список эпиков не пуст после очистки.");

        // Проверяем, что все подзадачи также удалены
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст после очистки эпиков.");
    }

    @Test
    void testClearSubtasks() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);

        // Создаем подзадачи и добавляем их
        Subtask subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description of Subtask 2", epic.getId());
        taskManager.appendSubtask(subtask1);
        taskManager.appendSubtask(subtask2);

        // Очищаем все подзадачи
        taskManager.clearSubtasks();

        // Проверяем, что все подзадачи удалены
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст после очистки.");

        // Проверяем, что список подзадач эпика пуст
        assertTrue(taskManager.getEpicById(epic.getId()).getListSubtasks().isEmpty(),
                "Список подзадач эпика не пуст после очистки подзадач.");
    }

    @Test
    void testDeleteTaskAndHistory() {
        // Создаем задачу
        Task task = new Task("Task 1", "Description of Task 1");
        taskManager.appendTask(task);

        // Добавляем задачу в историю
        taskManager.getTaskById(task.getId());

        // Проверяем, что задача есть в истории
        assertFalse(taskManager.getHistory().isEmpty(), "История пуста после добавления задачи.");

        // Удаляем задачу
        taskManager.deleteTask(task.getId());

        // Проверяем, что задача удалена из истории
        assertTrue(taskManager.getHistory().isEmpty(), "Задача не была удалена из истории.");
    }

}

