package ru.akirakozov.sd.app.store;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.akirakozov.sd.app.errors.StockRuntimeException;

import javax.sql.DataSource;
import java.util.List;

public class StockStoreJdbc extends JdbcDaoSupport implements StockStore {


    public StockStoreJdbc(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        createTables();
    }

    private void createTables() {
        String sqlCreateStockTable = "create table if not exists stock_table \n" +
                "(\n" +
                "    company       text    not null\n" +
                "        constraint stock_table_pk\n" +
                "            primary key,\n" +
                "    shares_number integer default 0 not null,\n" +
                "    shares_price  integer not null\n" +
                ");";
        getJdbcTemplate().execute(sqlCreateStockTable);
    }


    @Override
    public void storeCompany(String companyName, int sharesPrice) {
        if (!haveCompany(companyName)) {
            String sql = "insert into stock_table(company, shares_number, shares_price) values (?, ?, ?)";
            getJdbcTemplate().update(sql, companyName, 0, sharesPrice);
        } else {
            throw new StockRuntimeException("Already have such company");
        }
    }

    private boolean haveCompany(String companyName) {
        return getCompanyInfo(companyName) != null;
    }

    private SqlCompanyShares getCompanyInfo(String companyName) {
        String sql_user_events = "select * from stock_table where company=\"" + companyName + "\"";
        List<SqlCompanyShares> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(SqlCompanyShares.class));
        if (lists.isEmpty()) {
            return null;
        }
        return lists.get(0);
    }

    private SqlCompanyShares getCompanyInfoNotNull(String companyName) {
        String sql_user_events = "select * from stock_table where company=\"" + companyName + "\"";
        List<SqlCompanyShares> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(SqlCompanyShares.class));
        if (lists.isEmpty()) {
            throw new StockRuntimeException("No such company");
        }
        return lists.get(0);
    }


    @Override
    public void addShares(String companyName, int sharesNumber) {
        if (!haveCompany(companyName)) {
            throw new StockRuntimeException("No such company");
        }
        String sql = "update stock_table set shares_number = shares_number + " + sharesNumber + " where company=\"" + companyName + "\"";
        getJdbcTemplate().update(sql);
    }

    @Override
    public int getSharesNumber(String companyName) {
        SqlCompanyShares companyInfo = getCompanyInfoNotNull(companyName);
        return companyInfo.getShares_number();
    }

    @Override
    public int getSharesPrice(String companyName) {
        SqlCompanyShares companyInfo = getCompanyInfoNotNull(companyName);
        return companyInfo.getShares_price();
    }

    @Override
    public boolean buyShares(String companyName, int sharesNumber) {
        SqlCompanyShares companyInfo = getCompanyInfoNotNull(companyName);
        if (companyInfo.getShares_number() < sharesNumber) {
            return false;
        }
        String sql = "update stock_table set shares_number = shares_number - " + sharesNumber
                + " where company=\"" + companyName + "\"";
        getJdbcTemplate().update(sql);
        return true;
    }

    @Override
    public void changeSharesPrice(String companyName, int newPrice) {
        getCompanyInfoNotNull(companyName);
        String sql = "update stock_table set shares_price = " + newPrice + "" +
                " where company=\"" + companyName + "\"";
        getJdbcTemplate().update(sql);
    }
}
