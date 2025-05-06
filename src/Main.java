
import app.manager.FileBackedTaskManager;
import app.manager.HistoryManager;
import app.manager.Managers;
import app.manager.TaskManager;
import app.serverHttp.HttpTaskServer;
import app.tasks.Epic;
import app.tasks.Subtask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public class Main {

    private static final File FILE_PATH = new File("tasks.csv");  // Файл для сохранения данных

    public static void main(String[] args) throws IOException {

        TaskManager manager;
        String hostName = "127.0.0.1";
        Integer port = 8080;
        HttpTaskServer httpTaskServer;

        //Создаем менеджер или загружаем историю
        if (Files.exists(FILE_PATH.toPath())) {
            manager = FileBackedTaskManager.loadFromFile(FILE_PATH);
        } else {
            HistoryManager historyManager = Managers.getDefaultHistory();
            manager = new FileBackedTaskManager(historyManager, FILE_PATH.toPath());
        }

//        Создаем сервер и вызываем start
        httpTaskServer = new HttpTaskServer(hostName, port, manager);

        httpTaskServer.start();


//        Создаем эпик
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.appendEpic(epic1);

        // Создаем подзадачи для эпика
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId(),
                Instant.parse("2023-10-01T11:00:00Z"), Duration.ofMinutes(20));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId(),
                Instant.parse("2023-10-01T11:30:00Z"), Duration.ofMinutes(25));
        manager.appendSubtask(subtask1);
        manager.appendSubtask(subtask2);

    }

}