package ru.yandex.practicum.model;

import java.util.Objects;
public class Subtask extends Task {
    private final int epicId;

    public Subtask(String description, int EpicId) {
        super(description);
        this.epicId = EpicId;
    }

    public Subtask(Integer id,String name, String description, TaskStatus taskStatus, int EpicId) {
        super(id, name, description, taskStatus);
        this.epicId = EpicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), epicId);
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