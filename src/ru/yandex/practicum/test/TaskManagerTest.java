package ru.yandex.practicum.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.service.Managers.getDefault;

class TaskManagerTest {
    TaskManager manager = getDefault();

    @BeforeEach
    void beForEach() {

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
        Task task2 = new Task(TaskStatus.NEW, 1);
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
        Task testTask = new Task();
        manager.createTask(testTask);

        assertEquals(testTask, manager.getTaskById(testTask.getId()));

    }

    @Test
    void testSubtaskEqualityById() { //субтакс своим же эпиком
        Epic testEpic = new Epic();
        manager.createTask(testEpic);

        Subtask subtask = new Subtask(testEpic.getId());

        manager.createTask(subtask);
        assertEquals(subtask, manager.getTaskById(subtask.getId()));
    }

    @Test
    void testEpicEqualityById() {
        Epic testEpic = new Epic();
        manager.createTask(testEpic);
        assertEquals(testEpic, manager.getTaskById(testEpic.getId()));
    }

    @Test
    void checkForIdConflicts() {
        Task testTask1 = new Task();
        Task testTask2 = new Task();

        manager.createTask(testTask1);
        manager.createTask(testTask2);

        Assertions.assertNotEquals(manager.getTaskById(testTask1.getId()), manager.getTaskById(testTask2.getId()));

    }

    @Test
    void checkHistoryManagerSavesTaskVersions() {
        Task chekTask = new Task();
        manager.createTask(chekTask);
        manager.getTaskById(chekTask.getId());
        Task testTask = new Task(TaskStatus.IN_PROGRESS, chekTask.getId());
        manager.updateTask(testTask);
        manager.getTaskById(chekTask.getId());
        assertEquals(chekTask, manager.getHistory().getFirst());

    }

    @Test
    void addNewTask() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task();
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic();
        taskManager.createEpic(epic);

        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    public void deleteAllTasks() {
        manager.deleteAllTasks();
        List<Task> tasks = manager.getAllTasks();
        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    public void deleteAllEpics() {
        manager.deleteAllEpics();
        List<Epic> tasks = manager.getAllEpics();
        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    public void deleteAllSubtask() {
        manager.deleteAllSubtasks();
        List<Subtask> tasks = manager.getAllSubtasks();
        Assertions.assertEquals(0, tasks.size());
    }
}