package app.handlers;

import app.exception.ErrorResponse;
import app.exception.TaskNotFoundException;
import app.manager.TaskManager;
import app.tasks.Subtask;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpSubtaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HttpSubtaskHandler(TaskManager manager) {
        this.taskManager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    this.handleGet(exchange);
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
            Integer idSubtask = Integer.parseInt(urlParts[2]);
            Subtask removeSubtask = taskManager.deleteSubtask(idSubtask);
            String jsonString = jsonMapper.toJson(idSubtask);
            sendText(exchange, String.format("Задача:\n %s \n успешно удалена", jsonString), 200);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
        String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
        Subtask subtask = jsonMapper.fromJson(bodyString, Subtask.class);
        //Пррровести проверку времени если начало одна точка
//        тогда даю 406 ошибку ! пgetStartTime + Duration и если
//        что бы конец задачи не заходил на новую задачу старта
        if (subtask.getId() == null) {
            taskManager.appendSubtask(subtask);
        }else {
            taskManager.updateSubtask(subtask);
        }
        String stringJson = jsonMapper.toJson(subtask);
        sendText(exchange, stringJson, 201);
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String path = requestUri.getPath();
        String[] urlParts = path.split("/");

        if (urlParts.length == 3) {
            Integer id = Integer.valueOf(urlParts[2]);
            Subtask subtaskById = taskManager.getSubtaskById(id);
            String stringJson = jsonMapper.toJson(subtaskById);
            sendText(exchange, stringJson, 200);
        }

        if (urlParts.length == 2) {
            List<Subtask> allSubtask = taskManager.getAllSubtasks();
            String jsonString = jsonMapper.toJson(allSubtask);
            sendText(exchange, jsonString, 200);
        }

    }

}