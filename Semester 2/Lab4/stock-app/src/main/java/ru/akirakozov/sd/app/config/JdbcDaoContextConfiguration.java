package ru.akirakozov.sd.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.akirakozov.sd.app.store.StockStore;
import ru.akirakozov.sd.app.store.StockStoreJdbc;

import javax.sql.DataSource;


@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public StockStore stockStoreJbdc(DataSource dataSource) {
        return new StockStoreJdbc(dataSource);
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
