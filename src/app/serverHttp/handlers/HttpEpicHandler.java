package app.serverHttp.handlers;

import app.exception.ErrorResponse;
import app.exception.TaskNotFoundException;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.Subtask;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpEpicHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HttpEpicHandler(TaskManager manager) {
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
                    sendText(exchange, errorStringJson, 405);
                    break;

            }

        } catch (TaskNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
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
            Epic removeEpic = taskManager.deleteEpic(idTask);
            String jsonString = jsonMapper.toJson(removeEpic);
            sendText(exchange, String.format("Задача:\n %s \n успешно удалена", jsonString), 200);
        }

    }

    private void handlePost(HttpExchange exchange) throws IOException {
        byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
        Epic appendEpic = jsonMapper.fromJson(bodyString, Epic.class);
        Epic epic = taskManager.appendEpic(appendEpic);
        String stringJson = jsonMapper.toJson(epic);
        sendText(exchange, stringJson, 201);
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String path = requestUri.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 4 && urlParts[3].equals("subtasks")) {
            Integer id = Integer.valueOf(urlParts[2]);
            Epic epicById = taskManager.getEpicById(id);
            List<Subtask> listSubtasksByEpic = epicById.getListSubtasks();
            String stringListSubtaskByEpic = jsonMapper.toJson(listSubtasksByEpic);
            sendText(exchange, stringListSubtaskByEpic, 200);
        }

        if (urlParts.length == 3) {
            Integer id = Integer.valueOf(urlParts[2]);
            Epic taskById = taskManager.getEpicById(id);
            String stringJson = jsonMapper.toJson(taskById);
            sendText(exchange, stringJson, 200);
        }

        if (urlParts.length == 2) {
            List<Epic> allEpic = taskManager.getAllEpic();
            String jsonString = jsonMapper.toJson(allEpic);
            sendText(exchange, jsonString, 200);
        }

    }

}