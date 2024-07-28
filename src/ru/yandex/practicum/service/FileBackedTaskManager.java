package ru.yandex.practicum.service;


import ru.yandex.practicum.model.*;


import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,status,type,epic\n");

            for (Task task : getTasks()) {
                bw.write(task.toString() + "\n");
            }

            for (Epic epic : getEpics()) {
                bw.write(epic.toString() + "\n");
            }
            for (Subtask subTask : getSubtasks()) {
                bw.write(subTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fm = new FileBackedTaskManager(file);
        Task task = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty() || !line.isBlank()) {
                    task = fromString(line);
                }
                switch (task.getTaskType()) {
                    case TASK:
                        tasks.put(task.getId(), task);
                    case EPIC:
                        epics.put(task.getId(), (Epic) task);
                    case SUBTASK:
                        assert task instanceof Subtask;
                        subtasks.put(task.getId(), (Subtask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения из файла.");
        }
        return fm;
    }

    public static Task fromString(String value) {
        String[] elements = value.split(",");
        int id = Integer.parseInt(elements[0]);
        TaskStatus taskStatus = TaskStatus.valueOf(elements[1]);
        TaskType type = TaskType.valueOf(elements[2]);

        switch (type) {
            case TASK:
                return new Task(taskStatus, id);
            case EPIC:
                return new Epic();
            case SUBTASK:
                int epicId = Integer.parseInt(elements[3]);
                return new Subtask(id, taskStatus, epicId);
            default:
                return null;
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

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        save();
        return super.getSubtaskById(id);
    }
}