package ru.yandex.practicum.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

public class InMemoryTaskManagerTest {
private TaskManager taskManager;
@BeforeEach
    public void addTaskTest(){
    taskManager = Managers.getDefault();
}
@Test
public void classManagersAddGoodInMemoryTaskManager(){
assertNotNull(taskManager, "Экземпляр класса не пронициализирован");
}
}

