package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DatabaseHandler;
import ru.akirakozov.sd.refactoring.html.HtmlHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractServlet extends HttpServlet {

    private final boolean isUpdating;

    public AbstractServlet(boolean isUpdatingDatabase) {
        isUpdating = isUpdatingDatabase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AtomicReference<String> result = new AtomicReference<>();
        String sql = getSqlQuery(request);
        if (sql == null) {
            result.set(getResultBody(null, request));
        } else {
            if (isUpdating) {
                DatabaseHandler.executeUpdateQuery(sql);
                result.set(getResultBody(null, request));
            } else {
                DatabaseHandler.executeQuery(sql, rs -> result.set(getResultBody(rs, request)));
            }
        }
        fillResponse(response, result.get());
    }

    protected abstract String getSqlQuery(HttpServletRequest request);

    protected abstract String getResultBody(ResultSet rs, HttpServletRequest request);

    protected String constructHtmlBody(boolean containsOneResultLine, ResultSet rs, String titleLine) {
        try {
            HtmlHandler htmlHandler = new HtmlHandler();
            if (titleLine != null) {
                htmlHandler.appendLine(titleLine);
            }
            if (containsOneResultLine) {
                htmlHandler.appendLine(Integer.toString(rs.getInt(1)));
            } else {
                while (rs.next()) {
                    htmlHandler.appendNamePriceInfo(rs.getString("name"), rs.getInt("price"));
                }
            }
            return htmlHandler.constructBody();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillResponse(HttpServletResponse response, String result) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(result);
    }
}
