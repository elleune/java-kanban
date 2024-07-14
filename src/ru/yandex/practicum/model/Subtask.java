package ru.yandex.practicum.model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicsId;


    public Subtask(int id, TaskStatus taskStatus, int EpicsId) {
        super(taskStatus, id);
        this.epicsId = EpicsId;
    }

    public Subtask(int id) {
    }


    public int getEpicId() {
        return epicsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicsId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                '}';

    }
}