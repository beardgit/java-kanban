package app.handlers;

import app.enumeration.TypeTask;
import app.exception.ErrorResponse;
import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;
    private final Gson jsonMapper;

    public HttpTaskHandler(TaskManager manager, Gson jsonMapper) {
        this.taskManager = manager;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handeGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:

            }
        } catch (Exception e) {
            System.out.println("Ошибка обработки запроса " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendText(exchange, errorStringJson, 404);
        } finally {
            exchange.close();
        }


//        exchange.getResponseBody().write("Hello".getBytes(StandardCharsets.UTF_8));
//        exchange.getResponseBody().flush();
//        exchange.close();

    }

    private void handleDelete(HttpExchange exchange) {
    }

    private void handlePost(HttpExchange exchange) {
    }

    private void handeGet(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String path = requestUri.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 3) { // получение по id
            Integer id = Integer.valueOf(urlParts[2]);
            Task taskById = taskManager.getTaskById(id);
            String stringJson = jsonMapper.toJson(taskById);
            sendText(exchange, stringJson, 200);
        }
        if (urlParts.length == 2) {//получение всех задач

            List<Task> allTasks = taskManager.getAllTasks();
            String jsonString = jsonMapper.toJson(allTasks);
            sendText(exchange, jsonString, 200);

        }
    }
}
