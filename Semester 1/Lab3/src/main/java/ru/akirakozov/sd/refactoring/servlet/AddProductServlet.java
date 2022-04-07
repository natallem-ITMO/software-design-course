package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DatabaseHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {

    public AddProductServlet() {
        super(true);
    }

    @Override
    protected String getSqlQuery(HttpServletRequest request) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        return String.format(DatabaseHandler.ADD_PRODUCT_QUERY, name, price);
    }

    @Override
    protected String getResultBody(ResultSet rs, HttpServletRequest request) {
        return "OK" + System.lineSeparator();
    }
}
