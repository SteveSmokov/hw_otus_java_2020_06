package ru.otus.diplom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.models.TaskFile;
import ru.otus.diplom.properties.RedisDataSourceProperties;

@Configuration
public class RedisDataSourceConfig {

    @Autowired
    private RedisDataSourceProperties redisDataSourceProperties;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setPort(redisDataSourceProperties.getPort());
        connectionFactory.setHostName(redisDataSourceProperties.getHost());
        connectionFactory.setDatabase(redisDataSourceProperties.getDbIndex());
        connectionFactory.setTimeout(redisDataSourceProperties.getDbTimeOut());
        if (!redisDataSourceProperties.getDbPassword().isEmpty())
            connectionFactory.setPassword(redisDataSourceProperties.getDbPassword());
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
