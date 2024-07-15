package test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;
import ru.yandex.practicum.service.HistoryManager;
import ru.yandex.practicum.service.InMemoryHistoryManager;
import ru.yandex.practicum.service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task = new Task(TaskStatus.NEW,0);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращена.");
        assertEquals(1, history.size(), "Неверный размер истории.");
        assertEquals(task, history.get(0), "Задачи не совпадают.");
    }


 @Test
        void newHistoryManager() {
            HistoryManager historyManager = Managers.getDefaultHistory();

            assertNotNull(historyManager, "Менеджер не проинициализирован");
        }
    @Test
    public void testAddAndRemoveHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task( TaskStatus.NEW, 1);
        Task task2 = new Task( TaskStatus.IN_PROGRESS,2);

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));

        historyManager.remove(task1.getId());
        history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }
    @Test
    public void testRemoveDeleteTask() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task( TaskStatus.NEW, 1);
        historyManager.add(task1);
        historyManager.remove(2);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    public void testAddSameTaskMultipleTimes() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(TaskStatus.NEW,1);
        historyManager.add(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }
}
