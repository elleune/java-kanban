<<<<<<< HEAD
=======

>>>>>>> e71f4b7d00e4d84be96015b9208eccc2472a2320
package ru.yandex.practicum.model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic() {

    }
<<<<<<< HEAD

=======
>>>>>>> e71f4b7d00e4d84be96015b9208eccc2472a2320
    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int id) {
        subtaskIds.add(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
<<<<<<< HEAD
        return "Epic{" + "subtaskIds=" + subtaskIds + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", name='" + getName() + '\'' + ", status=" + getStatus() + '}';
=======
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                '}';
>>>>>>> e71f4b7d00e4d84be96015b9208eccc2472a2320
    }
}