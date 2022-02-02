package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DatabaseHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {

    private static final HashMap<String, String> SQL_QUERIES = new HashMap<>() {{
        put("max", DatabaseHandler.MAX_QUERY);
        put("min", DatabaseHandler.MIN_QUERY);
        put("sum", DatabaseHandler.SUM_QUERY);
        put("count", DatabaseHandler.COUNT_QUERY);
    }};

    private static final HashMap<String, String> COMMAND_PATTERN = new HashMap<>() {{
        put("max", "<h1>Product with max price: </h1>");
        put("min", "<h1>Product with min price: </h1>");
        put("sum", "Summary price: ");
        put("count", "Number of products: ");
    }};

    private static final HashSet<String> COMMAND_ONE_LINE_OUTPUT = new HashSet<>(Arrays.asList("sum", "count"));

    public QueryServlet() {
        super(false);
    }

    @Override
    protected String getResultBody(ResultSet rs, HttpServletRequest request) {
        String command = request.getParameter("command");
        if (COMMAND_PATTERN.containsKey(command)) {
            return constructHtmlBody(COMMAND_ONE_LINE_OUTPUT.contains(command), rs, COMMAND_PATTERN.get(command));
        }
        return "Unknown command: " + command + System.lineSeparator();
    }

    @Override
    protected String getSqlQuery(HttpServletRequest request) {
        return SQL_QUERIES.getOrDefault(request.getParameter("command"), null);
    }

}
