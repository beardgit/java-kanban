import app.adapter.DurationAdapter;
import app.adapter.InstantAdapter;
import app.handlers.*;
import app.manager.FileBackedTaskManager;
import app.manager.HistoryManager;
import app.manager.Managers;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.Subtask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public class Main {

    private static final File FILE_PATH = new File("tasks.csv");  // Файл для сохранения данных

    public static void main(String[] args) {

        try {

            TaskManager manager;
            if (Files.exists(FILE_PATH.toPath())) {
                manager = FileBackedTaskManager.loadFromFile(FILE_PATH);
            } else {
                HistoryManager historyManager = Managers.getDefaultHistory();
                manager = new FileBackedTaskManager(historyManager, FILE_PATH.toPath());
            }


            // Создаем эпик
            Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
            manager.appendEpic(epic1);

            // Создаем подзадачи для эпика
            Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId(),
                    Instant.parse("2023-10-01T11:00:00Z"), Duration.ofMinutes(20));
            Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId(),
                    Instant.parse("2023-10-01T11:30:00Z"), Duration.ofMinutes(25));
            manager.appendSubtask(subtask1);
            manager.appendSubtask(subtask2);


            Gson jsonMapper = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(Instant.class, new InstantAdapter())
                    .create();

            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
            HttpServer server = HttpServer.create(address, 0);

            server.createContext("/tasks", new HttpTaskHandler(manager, jsonMapper));
            server.createContext("/epics", new HttpEpicHandler(manager, jsonMapper));
            server.createContext("/subtasks", new HttpSubtaskHandler(manager, jsonMapper));
            server.createContext("/prioritized", new HttpPrioritizedHandler(manager, jsonMapper));
            server.createContext("/history", new HttpHistoryHandler(manager, jsonMapper));

            server.start();


        } catch (IOException e) {
            throw new RuntimeException(e);

        }


    }
}