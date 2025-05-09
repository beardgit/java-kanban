package app.serverHttp.handlers;

import app.manager.TaskManager;
import app.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class HttpPrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpPrioritizedHandler(TaskManager manager) {
        this.taskManager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (!"GET".equals(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Метод не поддерживается");
                return;
            }

            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

            String jsonResponse = jsonMapper.toJson(prioritizedTasks);

            sendText(exchange, jsonResponse, 200);

        } catch (Exception e) {
            sendError(exchange, 500, "Внутренняя ошибка сервера: " + e.getMessage());
        }

    }


}