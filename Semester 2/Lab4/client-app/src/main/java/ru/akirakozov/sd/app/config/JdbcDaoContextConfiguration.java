package ru.akirakozov.sd.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.akirakozov.sd.app.store.ClientStore;
import ru.akirakozov.sd.app.store.ClientStoreJdbc;
import ru.akirakozov.sd.app.store.MemoryClientStore;

import javax.sql.DataSource;


@Configuration
public class JdbcDaoContextConfiguration {

//    @Bean
//    public ClientStore stockStoreJbdc(DataSource dataSource) {
//        return new ClientStoreJdbc(dataSource);
//    }

    @Bean
    public ClientStore stockStoreJbdc() {
        return new MemoryClientStore();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:stock.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
