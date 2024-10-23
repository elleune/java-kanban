package ru.yandex.practicum.model;

import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private int epicId;


    public Subtask(int id, TaskStatus taskStatus, int epicId) {
        super(taskStatus, id);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean isSubtask() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return getId() == subtask.getId();
    }

    public Subtask(int id) {

    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return String.format("Subtask %d: %s (%s) [Epic: %s], StartTime: %s, Duration: %d minutes",
                getId(),
                getName(),
                getStatus(),
                (getStartTime() != null ? getStartTime().format(formatter) : "Не указано"),
                (getDuration() != null ? getDuration().toMinutes() : 0));
    }

    @Override
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return String.format("%d,%s,%s,%s,%s,%s,%d,%d",
                getId(),
                getClass().getSimpleName(),
                getName(),
                getStatus(),
                getDescription(),
                (getStartTime() != null ? getStartTime().format(formatter) : ""),
                (getDuration() != null ? getDuration().toMinutes() : 0),
                getEpicId());
    }
}