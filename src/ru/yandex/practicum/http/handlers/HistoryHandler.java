package ru.yandex.practicum.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.service.FileBackedTaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    public HistoryHandler(FileBackedTaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetHistory(exchange);
        } else {
            sendNotFound(exchange, "Не найден");
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = manager.getHistory();
        String response;
        if (history.isEmpty()) {
            response = "История пуста!";
        } else {
            response = gson.toJson(history);
        }
        sendText(exchange, response, 200);
    }
}