package app.serverHttp;

import app.manager.TaskManager;
import app.serverHttp.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final String HOST_NAME;
    private final Integer PORT;
    private final TaskManager manager;
    public HttpServer SERVER = null;


    public HttpTaskServer(String hostName, Integer port, TaskManager manager) {
        this.HOST_NAME = hostName;
        this.PORT = port;
        this.manager = manager;
    }


    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(HOST_NAME, PORT);
        SERVER = HttpServer.create(address, 0);
        SERVER.start();
        initializeContexts();
    }

    public void stop(int delay) {
        SERVER.stop(delay);
    }

    private void initializeContexts() {
        SERVER.createContext("/tasks", new HttpTaskHandler(manager));
        SERVER.createContext("/epics", new HttpEpicHandler(manager));
        SERVER.createContext("/subtasks", new HttpSubtaskHandler(manager));
        SERVER.createContext("/prioritized", new HttpPrioritizedHandler(manager));
        SERVER.createContext("/history", new HttpHistoryHandler(manager));
    }

}
