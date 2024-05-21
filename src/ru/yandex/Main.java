package ru.yandex;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.service.TaskManager;
import ru.yandex.practicum.model.TaskStatus;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = new TaskManager();


        Task task1 = new Task("Задача 1");
        Task task2 = new Task("Задача 2");
        manager.createTask(task1);
        manager.createTask(task2);



        Epic epic1 = new Epic("Эпик 1");
        Epic epic2 = new Epic("Эпик 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);


        Subtask subtask1 = new Subtask("Подзадача 1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", epic2.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        Task task3 = new Task(task1.getId(), "Обновленная Задача 1", "Новое описание 1", TaskStatus.DONE);
        Task task4 = new Task(task2.getId(), "Обновленная Задача 2", "Новое описание 2", TaskStatus.IN_PROGRESS);

        manager.updateTask(task3);
        manager.updateTask(task4);

        Subtask subtask4 = new Subtask(subtask1.getId(), "Обновленная Подзадача 1", "Описание подзадачи - 1", TaskStatus.IN_PROGRESS ,epic1.getId());
        Subtask subtask5 = new Subtask(subtask1.getId(), "Обновленная Подзадача 2", "Описание подзадачи - 2", TaskStatus.DONE, epic1.getId());
        Subtask subtask6 = new Subtask(subtask3.getId(), "Обновленная Подзадача 3", "Описание подзадачи - 3", TaskStatus.DONE, epic2.getId());

        manager.updateSubtask(subtask4);
        manager.updateSubtask(subtask5);
        manager.updateSubtask(subtask6);
        manager.updateEpic(epic1);
        manager.updateEpic(epic2);

        System.out.println();

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        System.out.println();

        manager.deleteTaskById(2);
        manager.deleteEpicById(4);
        manager.deleteSubtaskById(6);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        System.out.println("Удаляем все задачи");
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println("Список задач пуст");
    }
}
