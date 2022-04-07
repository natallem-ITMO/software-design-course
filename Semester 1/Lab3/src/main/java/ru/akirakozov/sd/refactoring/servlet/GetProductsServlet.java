package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DatabaseHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {
    public GetProductsServlet() {
        super(false);
    }

    @Override
    protected String getSqlQuery(HttpServletRequest request) {
        return DatabaseHandler.GET_PRODUCTS_QUERY;
    }

    @Override
    protected String getResultBody(ResultSet rs, HttpServletRequest request) {
        return constructHtmlBody(false, rs, null);
    }

}
