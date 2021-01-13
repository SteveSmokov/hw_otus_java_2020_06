package ru.otus.diplom.sheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ScheduledClientsTasks {
    @Autowired
    private ClientTaskRepository clientTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JiraIssuesCmp jiraIssuesCmp;


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
            if ((savedTask != null) && (savedTask.getJTaskName() != null) && (!savedTask.getJTaskName().isEmpty())) {
                checkRegisteredTask(task, savedTask);
            } else {
                newTaskRegister(task);
            }
        }
    }

    /**
     * Регистрация новой заявки клиента
     * @param task Заявка клиента
     */
    private void newTaskRegister(Task task) {
        task = jiraIssuesCmp.createJIRAIssue(task);
        taskRepository.save(task);
        if (task.getExist_tf() > 0) {
            List<ClientFile> clientFiles = clientTaskRepository.getFileListByTaskID(task.getId());
            for (ClientFile file : clientFiles
            ) {
                TaskFile taskFile = new TaskFile(file.getId(), file.getTsk_id(), file.getFile_name(), false);
                try {
                    registerTaskFile(task.getJTaskName(), taskFile, file.getFile_blob().readAllBytes());
                } catch (IOException e) {
                    log.error("newTaskRegister - "+e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Проверка на наличии необходимости обновить информацию по регистрированной заявке
     * @param task текущее состояние заявки клиента
     * @param savedTask последнее состояние зарегистрировнной заявки
     */
    private void checkRegisteredTask(Task task, Task savedTask) {
        if (task.getP_time_start().after(savedTask.getP_time_start())) {
            if (!task.getText().equals(savedTask.getText())) {
                savedTask.setText(task.getText());
                if (jiraIssuesCmp.addCommentToJIRAIssue(savedTask.getJTaskName(), savedTask.getText())) {
                    task.setJEditDate(new Date());
                }
            }
            if (!task.getPriority().equals(savedTask.getPriority())) {
                savedTask.setPriority(task.getPriority());
                if (jiraIssuesCmp.updateJIRAIssuePriority(savedTask.getJTaskName(),
                        TaskPriority.byValue(savedTask.getPriority()).getJiraValue()))
                    task.setJEditDate(new Date());
            }
            taskRepository.save(savedTask);
            if (savedTask.getExist_tf() > 0) {
                checkTaskFiles(savedTask.getId(), savedTask.getJTaskName());
            }
        }
    }

    /**
     * Проверка необходимости добавления прикрепленных файлов к клиентской заявке
     * @param taskId ИД клиентской заявки для запроса списка заявок
     * @param issueName номер заявки Jira для прикрепления к ней файлов
     */
    private void checkTaskFiles(Long taskId, String issueName) {
        List<ClientFile> clientFiles = clientTaskRepository.getFileListByTaskID(taskId);
        for (ClientFile file : clientFiles
        ) {
            try {
                TaskFile taskFile = taskRepository.getTaskFileById(file.getId());
                if (taskFile == null) {
                    taskFile = new TaskFile(file.getId(), file.getTsk_id(), file.getFile_name(), false);
                    registerTaskFile(issueName, taskFile, file.getFile_blob().readAllBytes());
                } else if ((taskFile != null) && (!taskFile.isRegistered())) {
                    registerTaskFile(issueName, taskFile, file.getFile_blob().readAllBytes());
                }
            } catch (IOException e) {
                log.error("checkTaskFiles - "+e.getMessage(), e);
            }
        }
    }

    /**
     * Процедура добавления файла к заявке Jira
     * @param issueName номер заявки Jira
     * @param taskFile информация по файлу
     * @param bytes payload файла
     */
    private void registerTaskFile(String issueName, TaskFile taskFile, byte[] bytes) {
        if (jiraIssuesCmp.addFileToJIRAIssue(issueName, taskFile.getFile_name(),
                bytes)) {
            taskFile.setRegistered(true);
            taskRepository.save(taskFile);
        }
    }
}
