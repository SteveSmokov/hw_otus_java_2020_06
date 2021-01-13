package ru.otus.diplom.cmp.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.diplom.cmp.JiraIssuesCmp;
import ru.otus.diplom.models.Comment;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.services.JiraRestClient;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class JiraIssuesCmpImpl implements JiraIssuesCmp {
    public static final String COMMENTS_FIELD_NAME = "comments";
    public static final String TOTAL_FIELD_NAME = "total";
    public static final String BODY_FIELD_NAME = "body";
    public static final String ID_FIELD_NAME = "id";
    public static final String KEY_FIELD_NAME = "key";
    public static final String ISSUES_FIELD_NAME = "issues";
    public static final String JIRA_QUERY_FIELD_NAME = "jql";
    public static final String FIELDS_FIELD_NAME = "fields";
    public static final String PRIORITY_FIELD_NAME = "priority";
    public static final String FIND_BY_TASK_ID_QUERY = "\"ID Ticket ODS\" ~ \"%s\"";
    public static final String PENDING_ISSUES_QUERY = "project = \"ЕИС ОФ АМПУ\" AND " +
            "status = \"In PENDING\" AND \"ID Ticket ODS\" is not EMPTY";
    private final Gson gson = new Gson();
    @Autowired
    private JiraRestClient jiraRestClient;

    @Override
    public Task createJIRAIssue(Task task) {
        log.debug("Create Jira issue");
        String taskJson = new Gson().toJson(task);
        log.debug(taskJson);
        String result = jiraRestClient.createJIRAIssue(taskJson);
        JsonReader jsonReader = Json.createReader(new StringReader(result));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        task.setJTaskId(object.getString(ID_FIELD_NAME));
        task.setJTaskName(object.getString(KEY_FIELD_NAME));
        task.setJEditDate(new Date());
        return task;
    }

    @Override
    public boolean addCommentToJIRAIssue(String issue, String comment) {
        JsonObject commentJson = Json.createObjectBuilder()
                .add(BODY_FIELD_NAME, comment)
                .build();
        return jiraRestClient.addCommentToJIRAIssue(issue, commentJson.toString());
    }

    @Override
    public List<Comment> getIssueAllComments(String issue) {
        log.debug("Get all issue comments");
        String result = jiraRestClient.getCommentsToJIRAIssue(issue);
        log.debug(result);
        String comments = Json.createReader(new StringReader(result)).readObject().get(COMMENTS_FIELD_NAME).toString();
        log.debug(comments);
        List<Comment> commentsList = Arrays.asList(gson.fromJson(comments, Comment[].class));
        return commentsList;
    }

    @Override
    public Comment getIssueLastComment(String issue) {
        log.debug("Get issue last comment");
        String result = jiraRestClient.getCommentsToJIRAIssue(issue);
        log.debug(result);
        JsonReader jsonReader = Json.createReader(new StringReader(result));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        Comment lastComment = null;
        int lastIndex = object.getInt(TOTAL_FIELD_NAME) - 1;
        if (lastIndex > - 1) {
            String lCcomment = Json.createReader(new StringReader(result)).readObject()
                    .getJsonArray(COMMENTS_FIELD_NAME).get(lastIndex).toString();
            log.debug(lCcomment);
            lastComment = gson.fromJson(lCcomment, Comment.class);
        }
        return  lastComment;
    }

    @Override
    public boolean addWatcherToJIRAIssue(String issue, String userLogin){
        return jiraRestClient.addWatchersToJIRAIssue(issue, "\""+userLogin+"\"");
    }

    @Override
    public boolean updateJIRAIssuePriority(String issue, String priority) {
        JsonObject prioritet = Json.createObjectBuilder()
                .add(FIELDS_FIELD_NAME,
                        Json.createObjectBuilder().add(PRIORITY_FIELD_NAME,
                                Json.createObjectBuilder().add(ID_FIELD_NAME, priority)))
                .build();
        return jiraRestClient.updateJIRAIssuePriority(issue, prioritet.toString());
    }

    @Override
    public boolean addFileToJIRAIssue(String issue, String fileName, byte[] file) {
        return jiraRestClient.addFileToJIRAIssue(issue, fileName, file);
    }

    @Override
    public List<Task> searchJiraIssuesInPending() {
        log.debug("Search Jira issues in Pending");
        JsonObject findQuery = Json.createObjectBuilder()
                .add(JIRA_QUERY_FIELD_NAME, PENDING_ISSUES_QUERY)
                .build();
        String result = jiraRestClient.searchJiraIssues(findQuery.toString());
        log.debug(result);
        String tasks = Json.createReader(new StringReader(result)).readObject().get(ISSUES_FIELD_NAME).toString();
        log.debug(tasks);
        List<Task> taskList = Arrays.asList(gson.fromJson(tasks, Task[].class));
        return taskList;
    }

    @Override
    public Task searchJiraIssuesByTaskID(String taskId) {
        log.debug("Search Jira issue by task ID");
        JsonObject findQuery = Json.createObjectBuilder()
                .add(JIRA_QUERY_FIELD_NAME, String.format(FIND_BY_TASK_ID_QUERY,taskId))
                .build();
        String result = jiraRestClient.searchJiraIssues(findQuery.toString());
        JsonReader jsonReader = Json.createReader(new StringReader(result));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        String taskStr = object.getJsonArray(ISSUES_FIELD_NAME).get(0).toString();
        log.debug("Get task by ID - {}", taskStr);
        Task task = gson.fromJson(taskStr, Task.class);
        return task;
    }
}
