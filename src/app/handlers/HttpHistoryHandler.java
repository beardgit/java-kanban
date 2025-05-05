package app.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;

public class HttpHistoryHandler extends BaseHttpHandler {
    private final Gson jsonMapper;


    public HttpHistoryHandler(Gson gson) {
        this.jsonMapper = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
