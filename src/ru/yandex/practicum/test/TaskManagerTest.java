package ru.yandex.practicum.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;
import ru.yandex.practicum.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.service.Managers.getDefault;

class TaskManagerTest {


    @BeforeEach
    void beforeEach() {
        TaskManager manager = getDefault();

        Task task1 = new Task();
        Task task2 = new Task();

        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getId());
        Subtask subtask2 = new Subtask(epic1.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

    }

    @Test
    public void taskEqual() {
        Task task1 = new Task(TaskStatus.NEW, 1);
        Task task2 = new Task(TaskStatus.NEW, 2);
        Assertions.assertNotEquals(task1, task2);
    }

    @Test
    void testEqualsSubtask() {

        Subtask subtask1 = new Subtask(1);
        Subtask subtask2 = new Subtask(1);

        assertEquals(subtask1, subtask2);

    }

    @Test
    void testEqualsEpic() {

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        assertEquals(epic1, epic2);

    }

    @Test
    void testTasksEqualityById() {
        TaskManager manager = getDefault();
        Task testTask = new Task();
        manager.createTask(testTask);
        assertEquals(testTask, manager.getTaskById(testTask.getId()));

    }

    @Test
    void testSubtaskEqualityById() {
        TaskManager manager = getDefault();

        Epic testEpic = new Epic();
        manager.createTask(testEpic);

        Subtask subtask = new Subtask(testEpic.getId());

        manager.createTask(subtask);
        assertEquals(subtask, manager.getTaskById(subtask.getId()));
    }

    @Test
    void testEpicEqualityById() {
        TaskManager manager = getDefault();
        Epic testEpic = new Epic();
        manager.createTask(testEpic);
        assertEquals(testEpic, manager.getTaskById(testEpic.getId()));
    }

    @Test
    void checkForIdConflicts() {
        TaskManager manager = getDefault();
        Task testTask1 = new Task();
        Task testTask2 = new Task();

        manager.createTask(testTask1);
        manager.createTask(testTask2);

        Assertions.assertNotEquals(manager.getTaskById(testTask1.getId()), manager.getTaskById(testTask2.getId()));

    }
}