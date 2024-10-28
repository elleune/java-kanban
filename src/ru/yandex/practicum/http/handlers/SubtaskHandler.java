package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.service.FileBackedTaskManager;
import ru.yandex.practicum.service.ManagerSaveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandler(FileBackedTaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetSubtask(exchange);
                break;
            case "POST":
                handlePostSubtask(exchange);
                break;
            case "DELETE":
                handleDeleteSubtask(exchange);
                break;
            default:
                sendNotFound(exchange, "Не найден");
        }
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Subtask subtask = manager.getSubtaskById(id);
            if (subtask != null) {
                String response = gson.toJson(subtask);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange, "Не найден");
            }
        } else {
            List<Subtask> subtasks = manager.getAllSubtasks();
            String response = gson.toJson(subtasks);
            sendText(exchange, response, 200);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),
                StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(java.util.stream.Collectors.joining());
            Subtask subtask = gson.fromJson(json, Subtask.class);

            if (subtask.getId() > 0) {
                manager.updateSubtask(subtask);
                sendText(exchange, gson.toJson(subtask), 200);
            } else {
                try {
                    int id = manager.createSubtask(subtask);
                    sendText(exchange, gson.toJson(subtask), 201);
                } catch (ManagerSaveException e) {
                    sendHasInteractions(exchange, "Не найден");
                }
            }
        } catch (ManagerSaveException e) {
            sendText(exchange, "Ошибка менеджера: " + e.getMessage(), 400);
        } catch (IOException e) {
            sendText(exchange, "Ошибка ввода/вывода: " + e.getMessage(), 500);
        } catch (Exception e) {
            sendText(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteSubtaskById(id);
            sendText(exchange, "Subtask с id " + id + " удален", 200);
        } else {
            sendNotFound(exchange, "Не найден");
        }
    }
}