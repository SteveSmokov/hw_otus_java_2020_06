package ru.otus.diplom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash("TaskFiles")
public class TaskFile implements Serializable {
    @Id
    private Long file_id;
    private Long task_id;
    private String file_name;
    private boolean registered = false;
}
