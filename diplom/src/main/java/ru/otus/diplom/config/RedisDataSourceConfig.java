package ru.otus.diplom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;

@Configuration
public class RedisDataSourceConfig {

    @Value("${sp.datasource.host}")
    private String host;
    @Value("${sp.datasource.port}")
    private int port;
    @Value("${sp.datasource.index:0}")
    private int dbIndex;
    @Value("${sp.datasource.timeout:60000}")
    private int dbTimeOt;
    @Value("${sp.datasource.password:}")
    private String dbPassword;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setPort(port);
        connectionFactory.setHostName(host);
        connectionFactory.setDatabase(dbIndex);
        connectionFactory.setTimeout(dbTimeOt);
        if (!dbPassword.isEmpty())
            connectionFactory.setPassword(dbPassword);
        return connectionFactory;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }

    @Bean(name = "taskTemplate")
    public RedisTemplate<Long, Task> redisTaskTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<Long, Task> template = new RedisTemplate<Long, Task>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

    @Bean(name = "taskFilesTemplate")
    public RedisTemplate<Long, TaskFile> redisTaskFilesTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<Long, TaskFile> template = new RedisTemplate<Long, TaskFile>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }
}
