import app.adapter.DurationAdapter;
import app.adapter.InstantAdapter;
import app.handlers.*;
import app.manager.Managers;
import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        try {
            HttpServer server = HttpServer.create(address, 0);
            Gson jsonMapper = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(Instant.class, new InstantAdapter())
                    .serializeNulls()
                    .create();

            TaskManager manager = Managers.getDefault();
            manager.appendTask(new Task("Реализовать эндпоинты", "ABC"));
            manager.appendTask(new Task("Запустить сервер", "CDE"));

            System.out.println(manager.getAllTasks());

            server.createContext("/tasks", new HttpTaskHandler(manager, jsonMapper));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }


    }
}