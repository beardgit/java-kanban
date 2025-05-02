package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private Path filePath;

    @BeforeEach
    void setup() throws IOException {
        File file = File.createTempFile("test", ".csv");
        filePath = file.toPath();
        taskManager = createTaskManager();
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(filePath);
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(Managers.getDefaultHistory(), filePath);
    }

    @Test
    void testSaveAndLoadEpicWithSubtask() {
        Epic epic = taskManager.appendEpic(new Epic("E", "D"));
        Subtask sub = taskManager.appendSubtask(new Subtask("S", "D", epic.getId()));
        taskManager.save();

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(filePath.toFile());

        assertEquals(1, loaded.getAllEpic().size());
        assertEquals(1, loaded.getAllSubtasks().size());
        assertTrue(loaded.getEpicById(epic.getId()).getListSubtasks().stream()
                .anyMatch(s -> s.getId().equals(sub.getId())));
    }

    @Test
    void testFileSaveException() {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(),
                Path.of("/invalid/path/tasks.csv"));
        Task task = new Task("Bad", "Path");
        assertThrows(RuntimeException.class, () -> manager.appendTask(task));
    }

    @Test
    void testFileLoadException() {
        File file = new File("/invalid/path/data.csv");
        assertThrows(RuntimeException.class, () -> FileBackedTaskManager.loadFromFile(file));
    }
}