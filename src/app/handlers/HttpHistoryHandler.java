package app.handlers;

import app.manager.HistoryManager;
import app.manager.TaskManager;
import app.tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.util.List;
import java.util.Set;

public class HttpHistoryHandler extends BaseHttpHandler {
    private final Gson jsonMapper;
    private final TaskManager manager;

    public HttpHistoryHandler(TaskManager manager, Gson gson) {
        this.jsonMapper = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> allHistory = manager.getHistory();
        String jsonString = jsonMapper.toJson(allHistory);
        sendText(exchange, jsonString, 200);
    }
}
