import app.enumeration.StatusTasks;
import app.handlers.*;
import app.manager.FileBackedTaskManager;
import app.manager.Managers;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        try {
            HttpServer server = HttpServer.create(address, 0);
            Gson jsonMapper = new Gson();
            server.createContext("/tasks", new HttpTaskHandler(Managers.getDefault(), jsonMapper));

            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }


    }
}