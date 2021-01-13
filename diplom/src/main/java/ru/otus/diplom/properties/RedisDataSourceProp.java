package ru.otus.diplom.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class RedisDataSourceProp {

    @Value("${sp.datasource.host}")
    private String host;
    @Value("${sp.datasource.port}")
    private int port;
    @Value("${sp.datasource.index:0}")
    private int dbIndex;
    @Value("${sp.datasource.timeout:60000}")
    private int dbTimeOut;
    @Value("${sp.datasource.password:}")
    private String dbPassword;
}
