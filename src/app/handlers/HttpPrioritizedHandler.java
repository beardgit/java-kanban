package app.handlers;

import app.exception.ErrorResponse;
import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

public class HttpPrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson jsonMapper;

    public HttpPrioritizedHandler(TaskManager manager, Gson jsonMapper) {
        this.taskManager = manager;
        this.jsonMapper = jsonMapper;
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

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        String response = jsonMapper.toJson(new ErrorResponse(message, code, exchange.getRequestURI().getPath()));
        sendText(exchange, response, code);
    }
}