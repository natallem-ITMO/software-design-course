package ru.akirakozov.sd.refactoring.servlet;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.BaseServerDBTest;
import ru.akirakozov.sd.refactoring.Utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class QueryServletTest extends BaseServerDBTest {
    private static final HashMap<String, String> commandPattern = new HashMap<>() {{
        put("max", "<h1>Product with max price: </h1>");
        put("min", "<h1>Product with min price: </h1>");
        put("sum", "Summary price: ");
        put("count", "Number of products: ");
        put("other", "Unknown command: other");
        put("unknown", "Unknown command: unknown");
        put(null, "Unknown command: null");
    }};

    private static final ArrayList<String> TEST_NAMES = new ArrayList<>(Arrays.asList(
            "potatoes", "tomatoes", "onion", "kale", "eggs", "knife"));
    private static final ArrayList<Long> TEST_PRICES = new ArrayList<>(Arrays.asList(
            200L, 400L, 100L, 500L, 400L, 2000L));


    @Before
    public void setUp() {
        Utils.clearDatabase();
    }

    @Test
    public void testMaxQuery() throws Exception {
        HttpClient client = Utils.getClient();
        String command = "max";
        HttpResponse<String> maxResponse = getQueryResponse(client, command);
        assertEquals(200, maxResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                constructExpectedBody(List.of(commandPattern.get(command))),
                maxResponse.body());
        SortedMap<Long, String> currentState = new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            Utils.addProduct(curName, curPrice, client);
            currentState.put(curPrice, curName);
            maxResponse = getQueryResponse(client, command);
            assertEquals(200, maxResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    constructExpectedBody(
                            List.of(commandPattern.get(command),
                                    currentState.get(currentState.firstKey()) + '\t' + currentState.firstKey() + "</br>")),
                    maxResponse.body());
        }
    }

    @Test
    public void testMinQuery() throws Exception {
        HttpClient client = Utils.getClient();
        String command = "min";
        HttpResponse<String> minResponse = getQueryResponse(client, command);
        assertEquals(200, minResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                constructExpectedBody(List.of(commandPattern.get(command))),
                minResponse.body());
        SortedMap<Long, String> currentState = new TreeMap<>();
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            Utils.addProduct(curName, curPrice, client);
            currentState.put(curPrice, curName);
            minResponse = getQueryResponse(client, command);
            assertEquals(200, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    constructExpectedBody(
                            List.of(commandPattern.get(command),
                                    currentState.get(currentState.firstKey()) + '\t' + currentState.firstKey() + "</br>")),
                    minResponse.body());
        }
    }

    @Test
    public void testSumQuery() throws Exception {
        HttpClient client = Utils.getClient();
        String command = "sum";
        HttpResponse<String> minResponse = getQueryResponse(client, command);
        assertEquals(200, minResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                constructExpectedBody(List.of(commandPattern.get(command), "0")),
                minResponse.body());
        long sum = 0L;
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            sum += curPrice;
            Utils.addProduct(curName, curPrice, client);
            minResponse = getQueryResponse(client, command);
            assertEquals(200, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    constructExpectedBody(
                            List.of(commandPattern.get(command),
                                    Long.toString(sum))),
                    minResponse.body());
        }
    }

    @Test
    public void testCountQuery() throws Exception {
        HttpClient client = Utils.getClient();
        String command = "count";
        HttpResponse<String> minResponse = getQueryResponse(client, command);
        assertEquals(200, minResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                constructExpectedBody(List.of(commandPattern.get(command), "0")),
                minResponse.body());
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            Utils.addProduct(curName, curPrice, client);
            minResponse = getQueryResponse(client, command);
            assertEquals(200, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    constructExpectedBody(
                            List.of(commandPattern.get(command),
                                    Integer.toString(i + 1))),
                    minResponse.body());
        }
    }

    @Test
    public void testWrongCommand() throws Exception {
        HttpClient client = Utils.getClient();
        List<String> wrongQueries = new ArrayList<>(Arrays.asList("other", "unknown", null));
        for (String command : wrongQueries) {
            HttpResponse<String> minResponse = getQueryResponse(client, command);
            assertEquals(200, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    commandPattern.get(command) + System.lineSeparator(),
                    minResponse.body());
        }
    }

    private String constructExpectedBody(List<String> lines) {
        StringBuilder builder = new StringBuilder("<html><body>" + System.lineSeparator());
        for (String line : lines) {
            builder.append(line).append(System.lineSeparator());
        }
        builder.append("</body></html>").append(System.lineSeparator());
        return builder.toString();
    }

    private HttpResponse<String> getQueryResponse(HttpClient client, String commandParameter) throws Exception {
        String uriString = Utils.HOST + Utils.SERVLETS_NAMES.QUERY.label;
        URI uri;
        if (commandParameter == null) {
            uri = new URIBuilder(uriString)
                    .build();
        } else {
            uri = new URIBuilder(uriString)
                    .addParameter("command", commandParameter)
                    .build();
        }
        return Utils.getResponse(client, uri);
    }
}