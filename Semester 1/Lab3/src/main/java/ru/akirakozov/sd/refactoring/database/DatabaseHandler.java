package ru.akirakozov.sd.refactoring.database;

import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class DatabaseHandler {

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
    public static final String ADD_PRODUCT_QUERY =
            "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%1$s\",%2$s)";
    public static final String GET_PRODUCTS_QUERY = "SELECT * FROM PRODUCT";
    public static final String MAX_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    public static final String MIN_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    public static final String SUM_QUERY = "SELECT SUM(price) FROM PRODUCT";
    public static final String COUNT_QUERY = "SELECT COUNT(*) FROM PRODUCT";

    public static void executeUpdateQuery(@NotNull String sql) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeQuery(String sql, SqlCallbackFunc<ResultSet> callback) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            callback.apply(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface SqlCallbackFunc<T> {
        void apply(T t) throws SQLException;
    }

}
