package ru.yandex.practicum.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;


class TaskManagerTest {
    @Test
    public void taskEqual() {
        Task task1 = new Task( TaskStatus.NEW, 1);
        Task task2 = new Task(TaskStatus.NEW, 1);
        Assertions.assertNotEquals(task1, task2);
    }
}