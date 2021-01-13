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
    public void save(TaskFile taskFile) {
        taskFilesRedisTemplate.opsForValue().set(taskFile.getFile_id(), taskFile);
    }

    @Override
    public Task getTaskById(long id) {
        return taskRedisTemplate.opsForValue().get(id);
    }

    @Override
    public TaskFile getTaskFileById(long file_id) {
        return taskFilesRedisTemplate.opsForValue().get(file_id);
    }
}
