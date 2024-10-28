package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.service.FileBackedTaskManager;
import ru.yandex.practicum.service.ManagerSaveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(FileBackedTaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGetTask(exchange);
                break;
            case "POST":
                handlePostTask(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            default:
                sendNotFound(exchange, "Не найдено");
        }
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Task task = null;
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            task = manager.getTaskById(id);
            if (task != null) {
                String response = gson.toJson(task);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange, "Не найдено");
            }
        } else {
            List<Task> tasks = manager.getAllTasks();
            String response = gson.toJson(tasks);
            sendText(exchange, response, 200);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),
                StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(java.util.stream.Collectors.joining());
            Task task = gson.fromJson(json, Task.class);

            if (task.getId() > 0) {
                manager.updateTask(task);
                sendText(exchange, gson.toJson(task), 200);
            } else {
                try {
                    int id = manager.createTask(task);
                    sendText(exchange, gson.toJson(task), 201);
                } catch (ManagerSaveException e) {
                    sendHasInteractions(exchange, "Не найдено");
                }
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, "Manager Error: " + e.getMessage(), 400);
        } catch (IOException e) {
            sendText(exchange, "Input/Output Error: " + e.getMessage(), 500);
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Task task = manager.getTaskById(id);
            if (task != null) {
                manager.deleteTaskById(id);
                sendText(exchange, "Task with id " + id + " deleted", 200);
            } else {
                sendText(exchange, "Task not found", 404);
            }
        } else {
            sendNotFound(exchange, "Не найдено");
        }
    }
}