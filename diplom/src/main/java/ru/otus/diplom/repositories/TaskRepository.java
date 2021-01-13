package ru.otus.diplom.repositories;

import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;

public interface TaskRepository {
    void save(Task task);
    void save(TaskFile taskFile);
    Task getTaskById(long id);
    TaskFile getTaskFileById(long file_id);
}
