package ru.yandex.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    public DateTimeFormatter dateTimeFormatter;
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int id;
    private LocalDateTime startTime;
    private Duration duration;


    public Task(TaskStatus taskStatus, int id) {
        this.taskStatus = taskStatus;
        this.id = id;
    }

    public Task() {

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public boolean isEpic() {
        return false;
    }

    public boolean isSubtask() {
        return false;
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
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return String.format("Task: %d,%s,%s,%s,%s,%d",
                getId(),
                getName(),
                getStatus(),
                getDescription(),
                (getStartTime() != null ? getStartTime().format(formatter) : ""),
                (getDuration() != null ? getDuration().toMinutes() : 0));
    }

    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return String.format("%d,%s,%s,%s,%s,%s,%d",
                getId(),
                getClass().getSimpleName(),
                getName(),
                getStatus(),
                getDescription(),
                (getStartTime() != null ? getStartTime().format(formatter) : ""),
                (getDuration() != null ? getDuration().toMinutes() : 0));
    }
}