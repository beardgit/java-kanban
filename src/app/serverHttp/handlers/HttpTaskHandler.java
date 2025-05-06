package app.serverHttp.handlers;

import app.exception.ErrorResponse;
import app.exception.TaskNotFoundException;
import app.manager.TaskManager;
import app.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HttpTaskHandler(TaskManager manager) {
        this.taskManager = manager;
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
                    sendError(exchange, 405, errorStringJson);
                    break;

            }

        } catch (TaskNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendError(exchange, errorResponse.getErrorCode(), errorStringJson);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 406, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendError(exchange, errorResponse.getErrorCode(), errorStringJson);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500, exchange.getRequestURI().getPath());
            String errorStringJson = jsonMapper.toJson(errorResponse);
            sendError(exchange, errorResponse.getErrorCode(), errorStringJson);
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
            sendText(exchange, String.format("Удаленная задача:\n %s ", jsonString), 200);
        }

    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // task  для дальнейшего распределения (id есть обновляем / id нет добавляем)
        Task task;
        byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);

        task = jsonMapper.fromJson(bodyString, Task.class);
        if (task.getId() != null) {
            task = taskManager.updateTask(task);
        } else {
            task = taskManager.appendTask(task);
        }
        String stringJson = jsonMapper.toJson(task);
        sendText(exchange, stringJson, 201);
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String path = requestUri.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 3) {
            Integer id = Integer.valueOf(urlParts[2]);
            Task taskById = taskManager.getTaskById(id);
            String stringJson = jsonMapper.toJson(taskById);
            sendText(exchange, stringJson, 200);
        }
        if (urlParts.length == 2) {
            List<Task> allTasks = taskManager.getAllTasks();
            String jsonString = jsonMapper.toJson(allTasks);
            sendText(exchange, jsonString, 200);
        }
    }

}
