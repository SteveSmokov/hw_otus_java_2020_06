package ru.otus.spring.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.entities.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.services.AdminUserDao;
import ru.otus.services.AdminUserDaoImpl;
import ru.otus.services.AdminUserService;
import ru.otus.services.AdminUserServiceImp;

@Configuration
public class Config {
    private final String ENTITIES_PATH = "ru.otus.entities";

    @Value("${cache.max.elements.size}")
    private int maxCacheSize;

    @Bean
    public HibernateUtils getAdminUserDao(){
        return new HibernateUtils(ENTITIES_PATH);
    }

    @Bean
    public HwCache<Long, User> getCacheByUser(){
        HwCache<Long, User> userHwCache = new MyCache<>(maxCacheSize);
        return userHwCache;
    }
}
