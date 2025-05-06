package app.serverHttp;

import app.manager.InMemoryTaskManager;
import app.serverHttp.handlers.BaseHttpHandler;
import app.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTests {

    private InMemoryTaskManager taskManager;
    private HttpTaskServer httpTaskServer;
    private int port;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager(null);
        port = findFreePort();
        httpTaskServer = new HttpTaskServer("localhost", port, taskManager);
        httpTaskServer.start();
    }

    private int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @AfterEach
    public void tearDown() {
        if (httpTaskServer != null) {
            httpTaskServer.stop(0);
        }
    }

    @Test
    public void testAddTask() throws Exception {
        Task task = new Task("Task 1", "Description 1", Instant.now(), Duration.ofMinutes(5));
        String taskJson = BaseHttpHandler.jsonMapper.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:" + port + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Статус ответа не соответствует ожидаемому");

        var tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("Task 1", tasks.get(0).getName(), "Некорректное имя задачи");
    }


    @Test
    public void testGetAllTasks() throws Exception {
        Task task = new Task("Task 1", "Description 1", Instant.now(), Duration.ofMinutes(5));
        taskManager.appendTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:" + port + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Статус ответа не соответствует ожидаемому");
        assertTrue(response.body().contains("Task 1"), "Ответ не содержит добавленную задачу");
    }
}