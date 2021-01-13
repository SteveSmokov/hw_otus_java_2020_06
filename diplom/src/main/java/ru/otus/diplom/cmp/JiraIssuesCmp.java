package ru.otus.diplom.cmp;

import ru.otus.diplom.models.Comment;
import ru.otus.diplom.models.Task;

import java.util.List;

public interface JiraIssuesCmp {
    Task createJIRAIssue(Task task);
    boolean addCommentToJIRAIssue(String issue, String comment);
    List<Comment> getIssueAllComments(String issue);
    Comment getIssueLastComment(String issue);
    boolean addWatcherToJIRAIssue(String issue, String userLogin);
    boolean updateJIRAIssuePriority(String issue, String priority);
    boolean addFileToJIRAIssue(String issue, String fileName, byte[] file);
    List<Task> searchJiraIssuesInPending();
    Task searchJiraIssuesByTaskID(String taskId);
}
