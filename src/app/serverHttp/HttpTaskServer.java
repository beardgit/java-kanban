package app.serverHttp;

import app.manager.TaskManager;
import app.serverHttp.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final String hostNabe;
    private final Integer port;
    private final TaskManager manager;
    public HttpServer server = null;


    public HttpTaskServer(String hostName, Integer port, TaskManager manager) {
        this.hostNabe = hostName;
        this.port = port;
        this.manager = manager;
    }


    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(hostNabe, port);
        server = HttpServer.create(address, 0);
        server.start();
        initializeContexts();
    }

    public void stop(int delay) {
        server.stop(delay);
    }

    private void initializeContexts() {
        server.createContext("/tasks", new HttpTaskHandler(manager));
        server.createContext("/epics", new HttpEpicHandler(manager));
        server.createContext("/subtasks", new HttpSubtaskHandler(manager));
        server.createContext("/prioritized", new HttpPrioritizedHandler(manager));
        server.createContext("/history", new HttpHistoryHandler(manager));
    }

}
