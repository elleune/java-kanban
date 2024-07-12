package ru.yandex.practicum.test;

import org.junit.jupiter.api.Test;


import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.service.HistoryManager;
import ru.yandex.practicum.service.Managers;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;





import java.util.List;





import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.service.Managers.getDefault;


public class HistoryManagerTest {

 @Test
        void newHistoryManager() {
            HistoryManager historyManager = Managers.getDefaultHistory();

            assertNotNull(historyManager, "Менеджер не проинициализирован");
        }
@Test
        void checkSizeOfRequestHistory() {
            HistoryManager historyManager = Managers.getDefaultHistory();
            Task task = new Task();
            final int sizeFromRequestHistoryShouldBe = 1;
            final int sizeForCheckRequestSize = 10;
            for (int i = 0; i <= sizeForCheckRequestSize; i++) {
                historyManager.add(task);
            }
            List<Task> exampleOfRequestHistoryList = historyManager.getHistory();

            assertEquals(sizeFromRequestHistoryShouldBe, exampleOfRequestHistoryList.size(), "Ограничение листа "
                    + "не работает");
        }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}
