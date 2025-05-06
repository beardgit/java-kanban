package app.handlers;

import app.adapter.DurationAdapter;
import app.adapter.InstantAdapter;
import app.exception.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public abstract class BaseHttpHandler implements HttpHandler {

    //Настраиваем Gson
     static Gson jsonMapper = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    protected void sendText(HttpExchange h, String text, Integer code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        String response = jsonMapper.toJson(new ErrorResponse(message, code, exchange.getRequestURI().getPath()));
        sendText(exchange, response, code);
    }

}
