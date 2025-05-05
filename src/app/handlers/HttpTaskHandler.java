package app.handlers;

import app.exception.ErrorResponse;
import app.exception.TaskNitFoundException;
import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

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
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    ErrorResponse errorResponse = new ErrorResponse(String.format("Обработка данного метода: %s - не предусмотрена", method), 405, exchange.getRequestURI().getPath());
                    String errorStringJson = jsonMapper.toJson(errorResponse);
                    sendText(exchange, errorStringJson, 405);
                    break;

            }
        } catch (TaskNitFoundException e) {
            System.out.println("Ошибка обработки запроса " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendText(exchange, errorStringJson, errorResponse.getErrorCode());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendText(exchange, errorStringJson, errorResponse.getErrorCode());
        } finally {
            exchange.close();
        }


    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String path = requestUri.getPath();
        String[] urlParts = path.split("/");
        if (urlParts.length == 3) {
            Integer idTask = Integer.parseInt(urlParts[2]);
            Task removeTask = taskManager.deleteTask(idTask);
            String jsonString = jsonMapper.toJson(removeTask);
            sendText(exchange, String.format("Задача:\n %s \n успешно удалена", jsonString), 200);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
        Task appendTask = jsonMapper.fromJson(bodyString, Task.class);
        Task task = taskManager.appendTask(appendTask);
        String stringJson = jsonMapper.toJson(task);
        sendText(exchange, stringJson, 201);
    }

    private void handleGet(HttpExchange exchange) throws IOException {
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
