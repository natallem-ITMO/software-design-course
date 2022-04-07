package ru.akirakozov.sd.app.store;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.akirakozov.sd.app.errors.ClientRuntimeException;
import ru.akirakozov.sd.app.model.Shares;
import ru.akirakozov.sd.app.model.User;

import javax.sql.DataSource;
import java.util.List;

public class ClientStoreJdbc extends JdbcDaoSupport implements ClientStore {

    public ClientStoreJdbc(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        createTables();
    }

    private void createTables() {
        String sqlCreateUserTable = "create table if not exists user_table\n" +
                "(\n" +
                "    id    text    not null\n" +
                "        constraint user_table_pk\n" +
                "            primary key,\n" +
                "    money integer not null\n" +
                ")";
        getJdbcTemplate().execute(sqlCreateUserTable);

        String sqlCreateUserShares =
                "create table if not exists user_shares\n" +
                        "(\n" +
                        "    user_id text    not null,\n" +
                        "    company text    not null,\n" +
                        "    number  integer not null,\n" +
                        "    constraint user_shares_pk\n" +
                        "        primary key (user_id, company)\n" +
                        ")";
        getJdbcTemplate().execute(sqlCreateUserShares);
    }

    @Override
    public void createUser(String userId) {
        if (!existsUser(userId)) {
            String sql = "insert into user_table(id, money) values (?, ?)";
            getJdbcTemplate().update(sql, userId, 0);
        } else {
            throw new ClientRuntimeException("Already have such user");
        }
    }

    @Override
    public void addMoney(String userId, int money) {
        getUserNotNull(userId);
        String sql = "update user_table set money = money + " + money
                + " where id=\"" + userId + "\"";
        getJdbcTemplate().update(sql);
    }

    @Override
    public void reduceMoney(String userId, int money) {
        getUserNotNull(userId);
        String sql = "update user_table set money = money - " + money
                + " where id=\"" + userId + "\"";
        getJdbcTemplate().update(sql);
    }

    @Override
    public List<Shares> getSharesOfUser(String userId) {
        String sql_user_events = "select * from user_shares where user_id=\"" + userId + "\"";
        List<Shares> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(Shares.class));
        return lists;
    }

    @Override
    public void buyShares(String userId, String companyName, int sharesNumber) {
        Shares shares = getUserShares(userId, companyName);
        if (shares != null) {
            String sql = String.format("update user_shares set number=number+%d where user_id=\"%s\" and company=\"%s\"", sharesNumber, userId, companyName);
            getJdbcTemplate().update(sql);
        } else {
            String sql = "insert into user_shares(user_id, company, number) values (?, ?, ?)";
            getJdbcTemplate().update(sql, userId, companyName, sharesNumber);
        }
    }

    @Override
    public void sellShares(String userId, String companyName, int sharesNumber) {
        Shares shares = getUserShares(userId, companyName);
        if (shares == null || shares.getNumber() < sharesNumber) {
            throw new ClientRuntimeException("Not enough shares to sell");
        } else {
            String sql = String.format("update user_shares set number=number-%d where user_id=\"%s\" and company=\"%s\"", sharesNumber, userId, companyName);
            getJdbcTemplate().update(sql);
        }
    }

    @Override
    public int getBalance(String userId) {
        return getUserNotNull(userId).getMoney();
    }

    private Shares getUserShares(String userId, String companyName) {
        String sql_user_events = "select * from user_shares where user_id=\"" + userId + "\" and company=\"" + companyName + "\"";
        List<Shares> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(Shares.class));
        return lists.isEmpty() ? null : lists.get(0);
    }

    private User getUserNotNull(String userId) {
        User user = getUser(userId);
        if (user == null) {
            throw new ClientRuntimeException("No such user");
        }
        return user;
    }

    private boolean existsUser(String userId) {
        return getUser(userId) != null;
    }

    private User getUser(String userId) {
        String sql_user_events = "select * from user_table where id=\"" + userId + "\"";
        List<User> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(User.class));
        if (lists.isEmpty()) {
            return null;
        }
        return lists.get(0);
    }
}
