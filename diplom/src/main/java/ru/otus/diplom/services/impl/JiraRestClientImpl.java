package ru.otus.diplom.services.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.otus.diplom.exceptions.HttpJiraServiceException;
import ru.otus.diplom.properties.JiraClientProp;
import ru.otus.diplom.services.CipherService;
import ru.otus.diplom.services.JiraRestClient;

import java.net.URI;
import java.util.Base64;

@Slf4j
@Service
public class JiraRestClientImpl implements JiraRestClient {

    private final String JIRA_SEARCH_PREFIX = "%s/rest/api/2/search";
    private final String JIRA_ISSUE_PREFIX = "%s/rest/api/2/issue";
    private final String JIRA_ISSUE_KEY_PREFIX = "%s/rest/api/2/issue/%s";
    private final String JIRA_ISSUE_COMMENT_PREFIX = "%s/rest/api/2/issue/%s/comment";
    private final String JIRA_ISSUE_WATCHERS_PREFIX = "%s/rest/api/2/issue/%s/watchers";
    private final String JIRA_ISSUE_ATACH_PREFIX = "%s/rest/api/2/issue/%s/attachments";

    @Autowired
    private JiraClientProp jiraClientProp;

    @Autowired
    private CipherService cipherService;

    /**
     * Создание заявки Jira
     * @param body тело заявки в формате JSON
     * @return ответ
     */
    @Override
    public String createJIRAIssue(String body) {
        log.debug("Create Jira issue");
        try {
            ResponseEntity<String> result = this.sendRequest(HttpMethod.POST,
                    String.format(JIRA_ISSUE_PREFIX,jiraClientProp.getJiraUrl()), body);
            if ((result.getStatusCode() != HttpStatus.OK) && (result.getStatusCode() != HttpStatus.CREATED))
                throw new HttpJiraServiceException(result.getStatusCode().toString()+" "+result.getBody());
            return result.getBody();
        } catch (HttpClientErrorException e) {
            log.error("createJIRAIssue: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    /**
     * Добавления комментария к указанной заявке JIRA
     *
     * @param issue номер заявки JIRA
     * @param body  тело запроса в формате JSON
     * @return добавлен или нет(true/false)
     */
    @Override
    public boolean addCommentToJIRAIssue(String issue, String body) {
        log.debug("Add comment to issue {}", issue);
        String fullUrl = String.format(JIRA_ISSUE_COMMENT_PREFIX,jiraClientProp.getJiraUrl(), issue);
        try {
            ResponseEntity<String> result = this.sendRequest(HttpMethod.POST, fullUrl, body);
            log.debug(result.getBody());
            return ((result.getStatusCode() == HttpStatus.OK) || (result.getStatusCode() == HttpStatus.CREATED));
        } catch (HttpClientErrorException e) {
            log.error("addCommentToJIRAIssue: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    @Override
    public String getCommentsToJIRAIssue(String issue) {
        log.debug("Create Jira issue");
        try {
            String fullUrl = String.format(JIRA_ISSUE_COMMENT_PREFIX, jiraClientProp.getJiraUrl(), issue);
            ResponseEntity<String> result = this.sendRequest(HttpMethod.GET, fullUrl, null);
            if (result.getStatusCode() != HttpStatus.OK)
                throw new HttpJiraServiceException(result.getStatusCode().toString()+" "+result.getBody());
            return result.getBody();
        } catch (HttpClientErrorException e) {
            log.error("getCommentsToJIRAIssue: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    /**
     * Добавления комментария к указанной заявке JIRA
     *
     * @param issue номер заявки JIRA
     * @param userLogin  логин пользователя который добавляется как наблюдатель
     * @return добавлен или нет(true/false)
     */
    @Override
    public boolean addWatchersToJIRAIssue(String issue, String userLogin) {
        log.debug("Add watcher to issue {}", issue);
        String fullUrl = String.format(JIRA_ISSUE_WATCHERS_PREFIX, jiraClientProp.getJiraUrl(), issue);
        try {
            ResponseEntity<String> result = this.sendRequest(HttpMethod.POST, fullUrl, userLogin);
            if (result.getStatusCode() != HttpStatus.NO_CONTENT)
                throw new HttpJiraServiceException(result.getStatusCode().toString()+" "+result.getBody());
            return true;
        } catch (HttpClientErrorException e) {
            log.error("addWatchersToJIRAIssue: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    @Override
    public boolean updateJIRAIssuePriority(String issue, String priority) {
        log.debug("Update priority to issue {}", issue);
        String fullUrl = String.format(JIRA_ISSUE_KEY_PREFIX, jiraClientProp.getJiraUrl(), issue);
        try {
            ResponseEntity<String> result = this.sendRequest(HttpMethod.PUT, fullUrl, priority);
            if (result.getStatusCode() != HttpStatus.NO_CONTENT)
                throw new HttpJiraServiceException(result.getStatusCode().toString()+" "+result.getBody());
            return true;
        } catch (HttpClientErrorException e) {
            log.error("updateJIRAIssuePriority: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    /**
     * Прикрепления файлов к заявке в JIRA
     * @param issue номер заявки JIRA
     * @param file файл
     * @return добавлен или нет(true/false)
     */
    @Override
    public boolean addFileToJIRAIssue(String issue, String fileName, byte[] file) {
        log.debug("Atach file to issue {}", issue);
        String fullUrl = String.format(JIRA_ISSUE_ATACH_PREFIX, jiraClientProp.getJiraUrl(), issue);
        try {
            String result = this.sendFile(fullUrl, fileName, file);
            log.debug("Response - {}",  result);
            return true;
        } catch (HttpClientErrorException e) {
            log.error("addFileToJIRAIssue: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    @Override
    public String searchJiraIssues(String body) {
        log.debug("Search Jira issues");
        try {
            ResponseEntity<String> result = this.sendRequest(HttpMethod.POST,
                    String.format(JIRA_SEARCH_PREFIX, jiraClientProp.getJiraUrl()), body);
            if (result.getStatusCode() != HttpStatus.OK)
                throw new HttpJiraServiceException(result.getStatusCode().toString()+" "+result.getBody());
            return result.getBody();
        } catch (HttpClientErrorException e) {
            log.error("searchJiraIssues: {}", e);
            throw new HttpJiraServiceException(e.getStatusText() + " " + e.getMessage());
        }
    }

    @SneakyThrows
    private ResponseEntity<String> sendRequest(HttpMethod method, String url, String body) {
        log.debug("Send request method - {} {}", method.name(), url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.ALL_VALUE);
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Connection", "keep-alive");
        headers.add("Cache-Control", "no-cache");
        headers.setBasicAuth(jiraClientProp.getJiraLogin(), cipherService.decrypt(jiraClientProp.getJiraPassword()));
        RestTemplate restTemplate;
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(600000);
        requestFactory.setConnectTimeout(600000);
        restTemplate = new RestTemplate(requestFactory);
        URI uri = new URI(url);
        HttpEntity<String> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, method, requestBody, String.class);
    }

    @SneakyThrows
    private String sendFile(String url, String fileName,  byte[] file) {
        log.debug("Add file - {}", url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        String basicAuth = jiraClientProp.getJiraLogin() + ":" + cipherService.decrypt(jiraClientProp.getJiraPassword());
        request.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicAuth.getBytes()));
        request.addHeader("X-Atlassian-Token","nocheck");
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ByteArrayBody fileBody = new ByteArrayBody(file, fileName);
        entity.addPart("file", fileBody);

        request.setEntity( entity);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(request);
            if ((response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) &&
                    (response.getStatusLine().getStatusCode() != HttpStatus.CREATED.value()))
                throw new HttpJiraServiceException(String.valueOf(response.getStatusLine().getStatusCode())+" "+
                        new String(response.getEntity().getContent().readAllBytes()));
            return new String(response.getEntity().getContent().readAllBytes());
        } finally {
            httpclient.close();
        }
    }
}
