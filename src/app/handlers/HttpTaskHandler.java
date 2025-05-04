package app.handlers;

import app.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpTaskHandler implements HttpHandler {

    private TaskManager taskManager;

    public HttpTaskHandler(TaskManager manager){
    this.taskManager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Отработано!");
        exchange.sendResponseHeaders(200,0);
        exchange.getResponseBody().write("Hello".getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().flush();
        exchange.close();

    }
}
