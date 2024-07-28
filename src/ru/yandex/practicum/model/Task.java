package ru.yandex.practicum.model;


import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int id;
    protected TaskType taskType;

    public Task(TaskStatus taskStatus, int id) {
        this.taskStatus = taskStatus;
        this.id = id;
    }

    public Task() {
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.taskStatus = status;
    }

    public int getId() {
        return id;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id && Objects.equals(description, task.description) && Objects.equals(name, task.name)
                && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, id, name, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + taskStatus +
                '}';
    }
}