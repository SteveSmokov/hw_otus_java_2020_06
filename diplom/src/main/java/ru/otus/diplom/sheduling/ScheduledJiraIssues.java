package ru.otus.diplom.sheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.diplom.cmp.JiraIssuesCmp;
import ru.otus.diplom.enums.Departament;
import ru.otus.diplom.enums.TaskPriority;
import ru.otus.diplom.models.ClientFile;
import ru.otus.diplom.models.Comment;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;
import ru.otus.diplom.repositories.ClientTaskRepository;
import ru.otus.diplom.repositories.TaskRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduledJiraIssues {
    @Autowired
    private ClientTaskRepository clientTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JiraIssuesCmp jiraIssuesCmp;

    /**
     * Обработка наших Jira заявок по таймеру, таймаут - fixedRate.jira.task.in.milliseconds
     */
    @Scheduled(fixedRateString = "${fixedRate.jira.task.in.milliseconds}")
    public void scheduleCheckOurJiraIssues() {
        log.info("Start check our Jira issues - " + System.currentTimeMillis() / 1000);
        List<Task> issues = jiraIssuesCmp.searchJiraIssuesInPending();
        for (Task task: issues
        ) {
            Task savedTask = taskRepository.getTaskById(task.getId());
            if ((savedTask != null) && (!savedTask.getWplan_prs_id().equals(Departament.SUPPORT.getValue()))){
                respondToSupport(savedTask);
            }
        }
    }

    private void respondToSupport(Task savedTask) {
        Comment comment = jiraIssuesCmp.getIssueLastComment(savedTask.getJtask_name());
        if ((comment != null) && (comment.getAuthor_name() != null) &&
                (comment.getText() != null)) {
            String taskText = savedTask.getText();
            taskText = taskText + "\n" + "--" + comment.getAuthor_name() +"\n"
                    + comment.getText();
            savedTask.setText(taskText);
            savedTask.setWplan_prs_id(Departament.SUPPORT.getValue());
            savedTask.setP_time_start(new Date());
            if(clientTaskRepository.updateTask(savedTask.getId(), savedTask.getWplan_prs_id(),
                    savedTask.getPriority(), savedTask.getText()))
                taskRepository.save(savedTask);
        }
    }
}
