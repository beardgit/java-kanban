package app.handlers;

import app.exception.ErrorResponse;
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

//    просмтреть метод запроса
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
         if( method.equalsIgnoreCase("GET")){
             List<Task> allHistory = manager.getHistory();
             String jsonString = jsonMapper.toJson(allHistory);
             sendText(exchange, jsonString, 200);
         }else {
             ErrorResponse errorResponse = new ErrorResponse(String.format("Обработка данного метода: %s - не предусмотрена", method), 405, exchange.getRequestURI().getPath());
             String errorStringJson = jsonMapper.toJson(errorResponse);
             sendText(exchange, errorStringJson, 405);
         }
//        Выбрасываем исключеие !
    }

}
