package ru.otus.diplom.services;

public interface JiraRestClient {

    String createJIRAIssue(String body);
    boolean addCommentToJIRAIssue(String issue, String body);
    String getCommentsToJIRAIssue(String issue);
    boolean addWatchersToJIRAIssue(String issue, String userLogin);
    boolean updateJIRAIssuePriority(String issue, String priority);
    boolean addFileToJIRAIssue(String issue, String fileName, byte[] file);
    String searchJiraIssues(String body);
}
