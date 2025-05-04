package app.handlers;

import app.enumeration.TypeTask;
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
                    URI requestUri = exchange.getRequestURI();
                    String path = requestUri.getPath();
                    String[] urlParts = path.split("/");
//                if (urlParts.length == 3) { // получение по id
//return;
//                }
                    if (urlParts.length == 2) {//получение всех задач

                        List<Task> allTasks = taskManager.getAllTasks();
                        String jsonString = jsonMapper.toJson(allTasks);
                        System.out.println("123");
                        sendText(exchange, jsonString);

                    }
                    break;
                case "POST":
                    break;
                case "DELETE":
                    break;
                default:


            }
        }catch (Exception e){
            System.out.println("Ошибка обработки запроса " + e.getMessage());
        }


//        exchange.getResponseBody().write("Hello".getBytes(StandardCharsets.UTF_8));
//        exchange.getResponseBody().flush();
//        exchange.close();

    }
}
