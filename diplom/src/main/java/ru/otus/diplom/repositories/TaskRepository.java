package ru.otus.diplom.repositories;

import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;

public interface TaskRepository {
    void save(Task task);
    void update(Task task);
    void save(TaskFile taskFile);
    Task getTaskById(long id);
    TaskFile getTaskFileById(long task_id, long file_id);
    void registerTaskToJira(long id, String jtask_id, String jtask_name);
    void registerTaskFileToJira(long task_id, long file_id);
    void saveTaskFile(TaskFile taskFile);
}
