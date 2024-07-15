package ru.yandex.practicum.service;
<<<<<<< HEAD

=======
>>>>>>> e71f4b7d00e4d84be96015b9208eccc2472a2320
import ru.yandex.practicum.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}