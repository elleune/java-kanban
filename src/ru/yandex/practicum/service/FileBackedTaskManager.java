package ru.yandex.practicum.service;


import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.Subtask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.model.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String CSV_FILE = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(CSV_FILE);

            for (Task task : getTasks()) {
                writer.write(task.toString() + "\n");
            }

            for (Epic epic : getEpics()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(subtask.toString() + "\n");
            }

            writer.write("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fm = new FileBackedTaskManager(file);
        int currentId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while (br.ready()) {
                String currentLine = br.readLine();
                String[] taskParameters = currentLine.split(",");

                switch (taskParameters[1]) {
                    case "TASK":
                        Task task = fromString(currentLine);

                        if (task != null) {
                            int taskId = task.getId();
                            currentId = Math.max(taskId, currentId);

                            fm.tasks.put(taskId, task);
                        }

                        break;
                    case "EPIC":
                        Epic epic = (Epic) fromString(currentLine);

                        if (epic != null) {
                            int epicId = epic.getId();
                            currentId = Math.max(epicId, currentId);

                            fm.epics.put(epicId, epic);
                        }

                        break;
                    case "SUBTASK":
                        Subtask subtask = (Subtask) fromString(currentLine);

                        if (subtask != null) {
                            int subtaskId = subtask.getId();
                            int epicId = subtask.getEpicId();
                            currentId = Math.max(subtaskId, currentId);

                            fm.epics.get(epicId).addSubtaskId(subtaskId);
                            fm.subtasks.put(subtaskId, subtask);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения из файла.");
        }
        ++currentId;
        return fm;
    }

    public static Task fromString(String value) {
        String[] line = value.split(",");
        int id = Integer.parseInt(line[0]);
        String taskType = line[1];
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String description = line[4];

        switch (taskType) {
            case "TASK":
                Task task = new Task();
                task.setStatus(status);
                task.setId(id);
                return task;
            case "EPIC":
                Epic epic = new Epic();
                epic.setId(id);
                return epic;
            case "SUBTASK":
                int epicId = Integer.parseInt(line[5]);
                Subtask subtask = new Subtask(epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип: " + taskType);
        }
    }


    public static void main(String[] args) {
        try {
            final File file = File.createTempFile("fileTaskManager", ".tmp");
            final FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

            final Task task1 = new Task();
            final Task task2 = new Task();
            manager.createTask(task1);
            manager.createTask(task2);


            final Epic epic1 = new Epic();
            final Epic epic2 = new Epic();
            manager.createEpic(epic1);
            manager.createEpic(epic2);


            final Subtask subtask1 = new Subtask(epic1.getId());
            final Subtask subtask2 = new Subtask(epic1.getId());
            final Subtask subtask3 = new Subtask(epic2.getId());

            manager.createSubtask(subtask1);
            manager.createSubtask(subtask2);
            manager.createSubtask(subtask3);

            System.out.println(manager.getEpics());
            System.out.println(manager.getTasks());
            System.out.println(manager.getSubtasks());

            final Task task3 = new Task(TaskStatus.DONE, task1.getId());
            final Task task4 = new Task(TaskStatus.IN_PROGRESS, task2.getId());

            manager.updateTask(task3);
            manager.updateTask(task4);

            final Subtask subtask4 = new Subtask(subtask1.getId(), TaskStatus.IN_PROGRESS, epic1.getId());
            final Subtask subtask5 = new Subtask(subtask1.getId(), TaskStatus.DONE, epic1.getId());
            final Subtask subtask6 = new Subtask(subtask3.getId(), TaskStatus.DONE, epic2.getId());

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
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createTask(Task task) {
        save();
        return super.createTask(task);
    }

    @Override
    public int createEpic(Epic epic) {
        save();
        return super.createEpic(epic);
    }

    @Override
    public int createSubtask(Subtask subtask) {
        save();
        return super.createSubtask(subtask);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
        return newSubtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}