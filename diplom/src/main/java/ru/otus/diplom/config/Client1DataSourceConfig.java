package ru.otus.diplom.config;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.diplom.properties.Client1DataSourceProperties;
import ru.otus.diplom.services.CipherService;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class Client1DataSourceConfig {
    @Autowired
    private CipherService cipherService;

    @Autowired
    private Client1DataSourceProperties client1DataSourceProperties;

    @Bean(name = "ClientConnectionPool")
    @Primary
    public DataSource getDataSource() {
        PoolDataSource pds = null;
        try {
            pds = PoolDataSourceFactory.getPoolDataSource();

            pds.setConnectionFactoryClassName(client1DataSourceProperties.getDriverClassName());
            pds.setURL(client1DataSourceProperties.getUrl());
            pds.setUser(client1DataSourceProperties.getUsername());
            pds.setPassword(cipherService.decrypt(client1DataSourceProperties.getPassword()));
            pds.setMinPoolSize(client1DataSourceProperties.getMinPoolSize());
            pds.setInitialPoolSize(10);
            pds.setMaxPoolSize(client1DataSourceProperties.getMaxPoolSize());
        } catch (SQLException ea) {
            System.err.println("Error connecting to the database: " + ea.getMessage());
        }
        return pds;
    }
}
