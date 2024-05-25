package ru.yandex.practicum.Test;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.service.HistoryManager;
import ru.yandex.practicum.service.Managers;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    @Test
    void newHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Менеджер не проинициализирован");
    }
}
