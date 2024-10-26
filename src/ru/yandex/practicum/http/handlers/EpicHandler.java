package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.http.Adapter.DurationAdapter;
import ru.yandex.practicum.http.Adapter.LocalDateTimeAdapter;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.service.TaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetEpic(exchange);
                break;
            case "POST":
                handlePostEpic(exchange);
                break;
            case "DELETE":
                handleDeleteEpic(exchange);
                break;
            default:
                sendNotFound(exchange, "Не найден");
        }
    }
        private void handleGetEpic(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("id=")) {
                int id = Integer.parseInt(query.substring(3));
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    String response = gson.toJson(epic);
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange, "Не найден");
                }
            } else {
                List<Epic> epics = taskManager.getAllEpics();
                String response = gson.toJson(epics);
                sendText(exchange, response, 200);
            }
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),
                    StandardCharsets.UTF_8))) {
                String json = reader.lines().collect(Collectors.joining());
                Epic epic = gson.fromJson(json, Epic.class);

                if (epic.getId() > 0) {
                    taskManager.updateEpic(epic);
                    sendText(exchange, gson.toJson(epic), 200);
                } else {
                    int id = taskManager.createEpic(epic);
                    sendText(exchange, gson.toJson(epic), 201);
                }
            } catch (Exception e) {
                sendText(exchange, "Внутренняя ошибка сервера", 500);
            }
        }

        private void handleDeleteEpic(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("id=")) {
                int id = Integer.parseInt(query.substring(3));
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    taskManager.deleteEpicById(id);
                    sendText(exchange, "Epic с id " + id + " удален", 200);
                } else {
                    sendText(exchange, "Epic не найден", 404);
                }
            } else {
                sendNotFound(exchange, "Не найден");
            }
        }
    }