package ru.yandex.practicum.service;


import ru.yandex.practicum.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public File file;
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
                writer.write(task.toFileString() + "\n");
            }

            for (Epic epic : getEpics()) {
                writer.write(epic.toFileString() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(subtask.toFileString() + "\n");
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

                final Task task = fromString(currentLine);
                int taskId = task.getId();
                currentId = Math.max(taskId, currentId);
                switch (task.getType()) {
                    case TaskType.TASK:
                        fm.tasks.put(taskId, task);
                        break;
                    case TaskType.EPIC:
                        fm.epics.put(taskId, (Epic) task);
                        break;
                    case TaskType.SUBTASK:
                        fm.subtasks.put(taskId, (Subtask) task);
                        break;
                }
                if (currentId < taskId) {
                    currentId = taskId;
                }
            }
            fm.id = currentId;

            for (Subtask subtask : fm.subtasks.values()) {
                Epic epic = fm.epics.get(subtask.getEpicId());

                if (epic != null) {
                    epic.addSubtaskId(subtask.getId());
                }
            }
            String history = br.readLine();
            if (history == null) {
                return fm;
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения из файла.");
        }
        return fm;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        return tasks.stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    // Метод восстановления истории из строки
    public static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(","))
                .filter(part -> !part.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static Task fromString(String value) {
        String[] line = value.split(",");
        int id = Integer.parseInt(line[0]);
        TaskType taskType = TaskType.valueOf(line[1]);
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String description = line[4];
        Duration duration = Duration.ofMinutes(Integer.parseInt(line[5]));
        LocalDateTime startTime = LocalDateTime.parse(line[6]);

        switch (taskType) {
            case TaskType.TASK:
                Task task = new Task();
                task.setId(id);
                task.setName(name);
                task.setStatus(status);
                task.setDescription(description);
                task.setDuration(duration);
                task.setStartTime(startTime);
                return task;
            case TaskType.EPIC:
                Epic epic = new Epic();
                epic.setId(id);
                epic.setName(name);
                epic.setStatus(status);
                epic.setDescription(description);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                return epic;
            case TaskType.SUBTASK:
                int epicId = Integer.parseInt(line[5]);
                Subtask subtask = new Subtask(epicId);
                subtask.setId(id);
                subtask.setName(name);
                subtask.setStatus(status);
                subtask.setDescription(description);
                subtask.setDuration(duration);
                subtask.setStartTime(startTime);
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