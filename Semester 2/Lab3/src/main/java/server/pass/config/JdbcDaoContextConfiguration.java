package server.pass.config;

import clock.Clock;
import clock.RealClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import store.EventStore;
import store.EventStoreJdbc;

import javax.sql.DataSource;


@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public EventStore eventStoreJbdc(DataSource dataSource) {
        return new EventStoreJdbc(dataSource);
    }

    @Bean
    public Clock clock() {
        return new RealClock();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:gym.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
