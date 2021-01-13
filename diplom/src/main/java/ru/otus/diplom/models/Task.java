package ru.otus.diplom.models;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.otus.diplom.adapters.TaskTypeAdapter;
import ru.otus.diplom.annotations.Column;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@RedisHash("Task")
@JsonAdapter(TaskTypeAdapter.class)
public class Task implements Serializable {
    private final String projectKey = "OFAMPU";
    private final String issueType = "10002";
    private final String regType = "10404";
    @Id
    @Column
    private Long id;
    private String jtask_id;
    private String jtask_name;
    @Column
    private Integer priority;
    @Column
    private Long wplan_prs_id;
    @Column
    private Long cmp_id;
    @Column
    private Long apl_id;
    @Column
    private String apl_name;
    @Column
    private String title;
    @Column
    private String text;
    @Column
    private String prs_name;
    @Column
    private Date prev_start_task;
    @Column
    private Date p_time_start;
    @Column
    private Integer exist_tf;
    private Date jedit_date;
}
