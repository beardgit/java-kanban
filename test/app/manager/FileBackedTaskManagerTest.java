package app.manager;

import app.enumeration.TypeTask;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private Path tempFilePath;

    @BeforeEach
    void setUp() throws IOException {

        // Создаем временный файл для тестов
        File tempFile = File.createTempFile("test", ".csv");
        tempFilePath = tempFile.toPath();
        taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), tempFilePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем временный файл после тестов
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        // Проверяем сохранение и загрузку пустого файла
        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFilePath.toFile());

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Задачи не должны быть загружены из пустого файла.");
        assertTrue(loadedManager.getAllEpic().isEmpty(), "Эпики не должны быть загружены из пустого файла.");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Подзадачи не должны быть загружены из пустого файла.");
    }

    @Test
    void testSaveAndLoadSingleTask() {
        // Добавляем одну задачу
        Task task = new Task("Task 1", "Description of Task 1");
        taskManager.appendTask(task);

        // Сохраняем и загружаем
        taskManager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFilePath.toFile());

        // Проверяем загруженные данные
        List<Task> tasks = loadedManager.getAllTasks();
        assertEquals(1, tasks.size(), "Должна быть загружена одна задача.");
        assertEquals("Task 1", tasks.get(0).getName(), "Название задачи должно совпадать.");
        assertEquals("Description of Task 1", tasks.get(0).getDescription(), "Описание задачи должно совпадать.");
    }

    @Test
    void testSaveAndLoadEpicWithSubtask() {
        // Добавляем эпик и подзадачу
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.appendEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        taskManager.appendSubtask(subtask);


        // Сохраняем и загружаем
        taskManager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFilePath.toFile());

        // Проверяем загруженные данные
        List<Epic> epics = loadedManager.getAllEpic();
        List<Subtask> subtasks = loadedManager.getAllSubtasks();

        assertEquals(1, epics.size(), "Должен быть загружен один эпик.");
        assertEquals(1, subtasks.size(), "Должна быть загружена одна подзадача.");

        assertEquals("Epic 1", epics.get(0).getName(), "Название эпика должно совпадать.");
        assertEquals("Description of Epic 1", epics.get(0).getDescription(), "Описание эпика должно совпадать.");

        assertEquals("Subtask 1", subtasks.get(0).getName(), "Название подзадачи должно совпадать.");
        assertEquals("Description of Subtask 1", subtasks.get(0).getDescription(), "Описание подзадачи должно совпадать.");
        assertEquals(epic.getId(), subtasks.get(0).getEpicId(), "ID эпика подзадачи должен совпадать.");

        // Проверяем связь между эпиком и подзадачей
        Epic loadedEpic = epics.get(0);
        assertTrue(loadedEpic.getListSubtasks().contains(subtasks.get(0)), "Подзадача должна быть связана с эпиком.");
    }

    @Test
    void testGetAllMethod() {
        // Добавляем задачи
        Task task = new Task("Task 1", "Description of Task 1");
        Epic epic = new Epic("Epic 1", "Description of Epic 1");
        Subtask subtask = new Subtask("Subtask 1", "Description of Subtask 1", epic.getId());
        taskManager.appendTask(task);
        taskManager.appendEpic(epic);
        taskManager.appendSubtask(subtask);

        // Вызываем метод getAll()
        Map<TypeTask, List<? extends Task>> allTasks = taskManager.getAll();

        // Проверяем содержимое
        assertNotNull(allTasks.get(TypeTask.TASK), "Список задач не должен быть null.");
        assertNotNull(allTasks.get(TypeTask.EPIC), "Список эпиков не должен быть null.");
        assertNotNull(allTasks.get(TypeTask.SUBTASK), "Список подзадач не должен быть null.");

        assertEquals(1, allTasks.get(TypeTask.TASK).size(), "Количество задач должно быть 1.");
        assertEquals(1, allTasks.get(TypeTask.EPIC).size(), "Количество эпиков должно быть 1.");
        assertEquals(1, allTasks.get(TypeTask.SUBTASK).size(), "Количество подзадач должно быть 1.");
    }

}