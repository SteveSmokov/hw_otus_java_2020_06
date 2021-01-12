package ru.otus.diplom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({@PropertySource(value="classpath:application.properties", ignoreResourceNotFound = true),
        @PropertySource(value="file:${CONFIG_FILE:application-clients.properties}", ignoreResourceNotFound = false)})
public class DiplomApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomApplication.class, args);
    }

}
