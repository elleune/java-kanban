package ru.yandex.practicum.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.http.handlers.EpicHandler;
import ru.yandex.practicum.http.handlers.HistoryHandler;
import ru.yandex.practicum.http.handlers.PrioritizedHandler;
import ru.yandex.practicum.http.handlers.SubtaskHandler;
import ru.yandex.practicum.http.handlers.TaskHandler;
import ru.yandex.practicum.service.FileBackedTaskManager;
import ru.yandex.practicum.service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final FileBackedTaskManager manager;
    private final HttpServer httpServer;
    private final Gson gson;

    public HttpTaskServer(FileBackedTaskManager manager) throws IOException {
        this.manager = manager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.gson = Managers.getGson();
    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHandler(manager, gson));
        httpServer.createContext("/epics", new EpicHandler(manager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager, gson));
        httpServer.createContext("/history", new HistoryHandler(manager, gson));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен!");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getFileBackedTaskManager());
        server.start();
    }
}