package ru.otus.diplom.repositories;



import ru.otus.diplom.models.ClientFile;
import ru.otus.diplom.models.Task;

import java.util.List;

public interface ClientTaskRepository {
    List<Task> getTasks();
    Task getTaskById(long id);
    List<ClientFile> getFileListByTaskID(long task_id);
    ClientFile getFileByID(long id);
    boolean updateTask(long id, long wplan_prs_id, int priority, String description);
    boolean updateClientFile(ClientFile clientFile);
}
