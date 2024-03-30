package elleune;

import elleune.model.Epic;
import elleune.model.Subtask;
import elleune.model.Task;
import elleune.model.TaskStatus;
import elleune.service.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = new TaskManager();

        System.out.println("Задачи");
        System.out.println("Создать задачи");
        manager.createTask(new Task("Сдать модуль по Java до 29.03.2024", "Task-1", TaskStatus.NEW));
        manager.createTask(new Task("Оправить письмо бабушке", "Task-2", TaskStatus.NEW));
        manager.printTasks();
        System.out.println("Показать задания");
        List<Task> taskList = manager.getAllTasks();
        System.out.println(taskList);
        System.out.println("Присвоить задаче номер");
        Task task = manager.getTaskById(1);
        System.out.println(task);
        System.out.println("Обновить задачу");
        task.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task);
        System.out.println(task);
        System.out.println();

        System.out.println("Epic");
        manager.createEpic(new Epic("Сдать работу по 4 спринту", "Epic-1", TaskStatus.NEW));
        manager.createEpic(new Epic("Распечатать фотографии", "Epic-2", TaskStatus.NEW));

        List<Epic> epics = manager.getAllEpics();
        System.out.println(epics);
        Epic epic = manager.getEpicById(3);
        System.out.println(epic);
        epic.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpic(epic);
        Epic epic3 = manager.getEpicById(3);
        System.out.println(epic3);
        System.out.println();

        System.out.println("*** Subtask ***");
        manager.createSubtask(new Subtask("Пить стакан теплой воды на тощак", "Subtask-1", TaskStatus.NEW, 3));
        manager.createSubtask(new Subtask("Не устраивать ночные дожоры", "Subtask-2", TaskStatus.NEW, 3));
        manager.createSubtask(new Subtask("Начать спать хотябы 6 часов в сутки", "Subtask-3", TaskStatus.NEW, 4));
        manager.createSubtask(new Subtask("Больше бывать на свежем воздухе", "Subtask-4", TaskStatus.NEW, 4));
        manager.printSubtasks();
        List<Subtask> subtasksByEpicId = manager.getAllSubtasksByEpicId(3);
        System.out.println(subtasksByEpicId);
        List<Subtask> subtasks = manager.getAllSubtasks();
        System.out.println(subtasks);
        Subtask subtask = manager.getSubtaskById(5);
        System.out.println(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask);
        System.out.println(subtask);
        System.out.println();

        System.out.println("Выполняем задачи");
        System.out.println("Выполнено");
        manager.deleteTaskById(1);
        System.out.println(taskList);
        System.out.println("Выполнено");
        manager.deleteAllTasks();
        manager.printTasks();
        System.out.println("Выполнено");
        manager.deleteSubtaskById(5);
        manager.printSubtasks();
        System.out.println("Выполнено");
        manager.deleteAllSubtasks();
        manager.printSubtasks();
        System.out.println("Выполнено");
        manager.deleteEpicById(4);
        manager.printEpics();
        System.out.println("Выполнено");
        manager.deleteAllEpics();
        manager.printEpics();
    }
}