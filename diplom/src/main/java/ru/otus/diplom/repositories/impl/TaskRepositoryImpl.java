package ru.otus.diplom.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;
import ru.otus.diplom.repositories.TaskRepository;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    @Qualifier("taskTemplate")
    private RedisTemplate<Long, Task> taskRedisTemplate;

    @Autowired
    @Qualifier("taskFilesTemplate")
    private RedisTemplate<Long, TaskFile> taskFilesRedisTemplate;

    @Override
    public void save(Task task) {
        taskRedisTemplate.opsForValue().set(task.getId(), task);
    }

    @Override
    public void update(Task task) {
        Task savedTask = taskRedisTemplate.opsForValue().get(task.getId());
        if (savedTask != null) {
            savedTask.setText(task.getText());
            savedTask.setPriority(task.getPriority());
            savedTask.setP_time_start(task.getP_time_start());
            savedTask.setJedit_date(task.getJedit_date());
        } else taskRedisTemplate.opsForValue().set(savedTask.getId(), savedTask);
    }

    @Override
    public void save(TaskFile taskFile) {
        taskFilesRedisTemplate.opsForValue().set(taskFile.getFile_id(), taskFile);
    }

    @Override
    public Task getTaskById(long id) {
        return taskRedisTemplate.opsForValue().get(id);
    }

    @Override
    public TaskFile getTaskFileById(long task_id, long file_id) {
        return (TaskFile) taskFilesRedisTemplate.opsForHash().get(task_id, file_id);
    }

    @Override
    public void registerTaskToJira(long id, String jtask_id, String jtask_name) {
        Task task = taskRedisTemplate.opsForValue().get(id);
        task.setJtask_id(jtask_id);
        task.setJtask_name(jtask_name);
        taskRedisTemplate.opsForValue().set(task.getId(), task);
    }



    @Override
    public void registerTaskFileToJira(long task_id, long file_id) {
        TaskFile file = (TaskFile) taskFilesRedisTemplate.opsForHash().get(task_id, file_id);
        file.setRegistered(true);
        taskFilesRedisTemplate.opsForHash().put(file.getTask_id(),
                file.getFile_id(), file);
    }

    @Override
    public void saveTaskFile(TaskFile taskFile) {
        taskFilesRedisTemplate.opsForHash().put(taskFile.getTask_id(),
                taskFile.getFile_id(), taskFile);
    }
}
