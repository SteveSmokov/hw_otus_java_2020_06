package ru.otus.diplom.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.diplom.cmp.impl.JiraIssuesCmpImpl;
import ru.otus.diplom.models.Comment;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.services.impl.CipherServiceImpl;
import ru.otus.diplom.services.impl.JiraRestClientImpl;
import ru.otus.diplom.utils.TaskUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest(classes = {JiraIssuesCmpImpl.class, CipherServiceImpl.class, JiraRestClientImpl.class})
@DisplayName("Тестирование компонента для работы с Jira")
class JiraIssuesCmpImplTest {

    @Autowired
    private CipherServiceImpl cipherService;

    @Autowired
    private JiraRestClientImpl jiraRestClient;

    @Autowired
    private JiraIssuesCmpImpl jiraIssuesCmp;

    @Test
    @DisplayName("Запрос списка заявок Jira в статусе ожидание(InPending)")
    void getIssuesListInPending() {
        List<Task> taskList = jiraIssuesCmp.searchJiraIssuesInPending();
        log.info(String.valueOf(taskList));
    }

    @Test
    void createJIRAIssue() {
        Task task = Task.builder()
                .id(1L)
                .priority(2)
                .title("Title")
                .text("Text")
                .wplan_prs_id(4798468L)
                .prev_start_task(new Date())
                .p_time_start(new Date())
                .apl_id(2L)
                .apl_name("Test application name")
                .prs_name("Test personal")
                .build();
        log.info(task.toString());
        task = jiraIssuesCmp.createJIRAIssue(task);
        log.info(task.toString());

    }

    @Test
    void addCommentToJIRAIssue() {
        boolean result = jiraIssuesCmp.addCommentToJIRAIssue("OFAMPU-533", "Test comment");
        Assertions.assertTrue(result);
    }

    @Test
    void addWatchersToJIRAIssue() {
        boolean result = jiraIssuesCmp.addWatcherToJIRAIssue("OFAMPU-533", "\"v.doichev\"");
        Assertions.assertTrue(result);
    }

    @Test
    void updateJIRAIssuePriority() {
        boolean result = jiraIssuesCmp.updateJIRAIssuePriority("OFAMPU-533", TaskUtil.getIDPriority(2));
        Assertions.assertTrue(result);
    }

    @Test
    void addFileToJIRAIssue() throws IOException {
        File initialFile = new File("D:\\Downloads_history\\ticket.pdf");
        InputStream targetStream = new FileInputStream(initialFile);
        boolean result = jiraIssuesCmp.addFileToJIRAIssue("OFAMPU-533", "test2.pdf",
                targetStream.readAllBytes());
        Assertions.assertTrue(result);
    }

    @Test
    void searchJiraIssuesInPending() {
        List<Task> issues = jiraIssuesCmp.searchJiraIssuesInPending();
        log.info(issues.toString());
    }

    @Test
    void searchJiraIssuesByTaskID() {
        Task issue = jiraIssuesCmp.searchJiraIssuesByTaskID("2");
        log.info(issue.toString());
    }

    @Test
    void getIssueAllComments() {
        List<Comment> comments = jiraIssuesCmp.getIssueAllComments("OFAMPU-533");
        log.info(comments.toString());
    }

    @Test
    void getIssueLastComment() {
        Comment comment = jiraIssuesCmp.getIssueLastComment("OFAMPU-533");
        log.info(comment.toString());
    }
}