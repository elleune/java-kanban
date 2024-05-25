package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;

import java.util.List;
import java.util.ArrayList;

public interface TaskManager {
    int generateId();

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateStatusEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);
    List<Task> getHistory();
    void remove(int id);
    ArrayList<Subtask> getEpicSubtasks(int epicId);

}
