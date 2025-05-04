package app.handlers;

import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskHandler extends BaseHttpHandler  {

    private TaskManager taskManager;
    private Gson jsonMapper;

    public HttpTaskHandler(TaskManager manager, Gson jsonMapper) {
        this.taskManager = manager;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Отработано!");
        exchange.sendResponseHeaders(200, 0);
//      Понять что нужно сделать полльзователь (по http методу)
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                URI requestUri = exchange.getRequestURI();
                String path = requestUri.getPath();
                String[] urlParts = path.split("/");
                if (urlParts.length == 3) { // получение по id

                }
                if (urlParts.length == 2) {//получение всех задач
                    List<Task> allTasks = taskManager.getAllTasks();
                    String jsonString = jsonMapper.toJson(allTasks);

                }
                break;
            case "POST":
                break;
            case "DELETE":
                break;
            default:


        }


//        exchange.getResponseBody().write("Hello".getBytes(StandardCharsets.UTF_8));
//        exchange.getResponseBody().flush();
//        exchange.close();

    }
}
