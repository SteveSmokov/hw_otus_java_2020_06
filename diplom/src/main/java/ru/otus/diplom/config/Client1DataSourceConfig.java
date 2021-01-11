package ru.otus.diplom.config;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.diplom.services.CipherService;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class Client1DataSourceConfig {
    @Autowired
    private CipherService cipherService;

    @Value("${spring.client1.datasource.url}")
    private String url;
    @Value("${spring.client1.datasource.username}")
    private String username;
    @Value("${spring.client1.datasource.password}")
    private String password;
    @Value("${spring.client1.datasource.minPoolSize:1}")
    private String minPoolSize;

    @Value("${spring.client1.datasource.maxPoolSize:10}")
    private String maxPoolSize;

    @Value("${spring.client1.datasource.driver-class-name:oracle.jdbc.pool.OracleDataSource}")
    private String driverClassName;

    @Bean(name = "ClientConnectionPool")
    @Primary
    public DataSource getDataSource() {
        PoolDataSource pds = null;
        try {
            pds = PoolDataSourceFactory.getPoolDataSource();

            pds.setConnectionFactoryClassName(driverClassName);
            pds.setURL(url);
            pds.setUser(username);
            pds.setPassword(cipherService.decrypt(password));
            pds.setMinPoolSize(Integer.valueOf(minPoolSize));
            pds.setInitialPoolSize(10);
            pds.setMaxPoolSize(Integer.valueOf(maxPoolSize));

        } catch (SQLException ea) {
            System.err.println("Error connecting to the database: " + ea.getMessage());
        }
        return pds;
    }
}
