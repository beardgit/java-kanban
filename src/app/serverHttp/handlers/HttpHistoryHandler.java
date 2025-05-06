package app.serverHttp.handlers;

import app.manager.TaskManager;
import app.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class HttpHistoryHandler extends BaseHttpHandler {

    private final TaskManager manager;

    public HttpHistoryHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Метод не поддерживается");
                return;
            }

            List<Task> allHistory = manager.getHistory();
            String jsonString = jsonMapper.toJson(allHistory);
            sendText(exchange, jsonString, 200);

        } catch (Exception e) {
            sendError(exchange, 500, "Внутренняя ошибка сервера: " + e.getMessage());
        }
    }

}
