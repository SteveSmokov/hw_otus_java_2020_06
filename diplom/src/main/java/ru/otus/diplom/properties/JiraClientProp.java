package ru.otus.diplom.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class JiraClientProp {

    @Value("${jira.client.url}")
    private String jiraUrl;
    @Value("${jira.client.login}")
    private String jiraLogin;
    @Value("${jira.client.password}")
    private String jiraPassword;
}
