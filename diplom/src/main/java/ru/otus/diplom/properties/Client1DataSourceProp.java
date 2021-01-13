package ru.otus.diplom.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class Client1DataSourceProp {

    @Value("${spring.client1.datasource.url}")
    private String url;

    @Value("${spring.client1.datasource.username}")
    private String username;

    @Value("${spring.client1.datasource.password}")
    private String password;

    @Value("${spring.client1.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.client1.datasource.minPoolSize}")
    private int minPoolSize;

    @Value("${spring.client1.datasource.maxPoolSize}")
    private int maxPoolSize;
}
