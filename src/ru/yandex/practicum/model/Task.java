package ru.yandex.practicum.model;

import java.util.Objects;

public class Task {
        private String description;
        private Integer id;
        private String name;
        private TaskStatus taskStatus;

    public Task(String description) { // для создания задачи
                this.description = description;
       }

    public Task(Integer id, String name, String description, TaskStatus taskStatus) { //  для обновления
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }


    public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TaskStatus getStatus() {
            return taskStatus;
        }

        public void setStatus(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
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