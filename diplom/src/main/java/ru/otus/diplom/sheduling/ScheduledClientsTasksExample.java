package ru.otus.diplom.sheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.diplom.cmp.JiraIssuesCmp;
import ru.otus.diplom.models.ClientFile;
import ru.otus.diplom.models.Comment;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;
import ru.otus.diplom.repositories.ClientTaskRepository;
import ru.otus.diplom.repositories.TaskRepository;
import ru.otus.diplom.utils.TaskUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduledClientsTasksExample {
    @Autowired
    private ClientTaskRepository clientTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JiraIssuesCmp jiraIssuesCmp;
    @Value("${client1_support.id}")
    private Long client_support_id;

    /**
     * Обработка клиентских заявок по таймеру, таймаут - fixedRate.client.task.in.milliseconds
     */
    @Scheduled(fixedRateString = "${fixedRate.client.task.in.milliseconds}")
    public void scheduleCheckClientTasks() {
        log.info("Start check client task - " + System.currentTimeMillis() / 1000);
        List<Task> tasks = clientTaskRepository.getTasks();
        for (Task task : tasks
        ) {
            Task savedTask = taskRepository.getTaskById(Long.valueOf(task.getId()));
            if ((savedTask != null) && (savedTask.getJtask_name() != null) && (!savedTask.getJtask_name().isEmpty())) {
                if (task.getP_time_start().after(savedTask.getP_time_start())) {
                    if (!task.getText().equals(savedTask.getText())) {
                        savedTask.setText(task.getText());
                        if (jiraIssuesCmp.addCommentToJIRAIssue(savedTask.getJtask_name(), savedTask.getText())) {
                            task.setJedit_date(new Date());
                        }
                    }
                    if (!task.getPriority().equals(savedTask.getPriority())) {
                        savedTask.setPriority(task.getPriority());
                        if (jiraIssuesCmp.updateJIRAIssuePriority(savedTask.getJtask_name(),
                                TaskUtil.getIDPriority(savedTask.getPriority())))
                            task.setJedit_date(new Date());
                    }
                    taskRepository.save(savedTask);
                    if (savedTask.getExist_tf() > 0) {
                        List<ClientFile> clientFiles = clientTaskRepository.getFileListByTaskID(savedTask.getId());
                        for (ClientFile file : clientFiles
                        ) {
                            TaskFile taskFile = taskRepository.getTaskFileById(file.getTsk_id(), file.getId());
                            if (taskFile != null) {
                                if (!taskFile.isRegistered()) {
                                    try {
                                        if (jiraIssuesCmp.addFileToJIRAIssue(savedTask.getJtask_name(), file.getFile_name(),
                                                file.getFile_blob().readAllBytes())) {
                                            taskFile.setRegistered(true);
                                            taskRepository.save(taskFile);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                taskFile = new TaskFile(file.getId(), file.getTsk_id(), file.getFile_name(), false);
                                try {
                                    if (jiraIssuesCmp.addFileToJIRAIssue(savedTask.getJtask_name(), file.getFile_name(),
                                            file.getFile_blob().readAllBytes())) taskFile.setRegistered(true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                taskRepository.save(taskFile);
                            }
                        }
                    }
                }
            } else {
                task = jiraIssuesCmp.createJIRAIssue(task);
                taskRepository.save(task);
                if (savedTask.getExist_tf() > 0) {
                    List<ClientFile> clientFiles = clientTaskRepository.getFileListByTaskID(savedTask.getId());
                    for (ClientFile file : clientFiles
                    ) {
                        TaskFile taskFile = new TaskFile(file.getId(), file.getTsk_id(), file.getFile_name(), false);
                        try {
                            if (jiraIssuesCmp.addFileToJIRAIssue(savedTask.getJtask_name(), file.getFile_name(),
                                    file.getFile_blob().readAllBytes())) taskFile.setRegistered(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        taskRepository.save(taskFile);
                    }
                }
            }
        }
    }

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
            if ((savedTask != null) && (!savedTask.getWplan_prs_id().equals(client_support_id))){
                Comment response = jiraIssuesCmp.getIssueLastComment(savedTask.getJtask_name());
                if ((response != null) && (response.getAuthor_name() != null) &&
                        (response.getText() != null)) {
                    String taskText = savedTask.getText();
                    taskText = taskText + "\n" + "--" + response.getAuthor_name() +"\n"
                            + response.getText();
                    savedTask.setText(taskText);
                    savedTask.setWplan_prs_id(client_support_id);
                    savedTask.setP_time_start(new Date());
                    if(clientTaskRepository.updateTask(savedTask.getId(), savedTask.getWplan_prs_id(),
                            savedTask.getPriority(), savedTask.getText()))
                        taskRepository.save(savedTask);
                }
            }
        }

    }
}
