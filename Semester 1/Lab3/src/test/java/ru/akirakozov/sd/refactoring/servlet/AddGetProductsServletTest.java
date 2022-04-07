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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AddGetProductsServletTest extends BaseServerDBTest {
    @Before
    public void setUp() {
        Utils.clearDatabase();
    }

    @Test
    public void testGetEmptyProductsResponse() throws Exception {
        checkGetResponse(constructExpectedBody(new ArrayList<>(), new ArrayList<>()), Utils.getClient());
    }

    @Test
    public void testNotEmptyProductsResponse() throws Exception {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("potatoes", "tomatoes", "onion", "kale", "eggs", "knife"));
        ArrayList<Long> prices = new ArrayList<>(Arrays.asList(200L, 400L, 100L, 500L, 400L, 2000L));
        assertEquals(names.size(), prices.size());
        Utils.clearDatabase();
        HttpClient client = Utils.getClient();
        for (int i = 0; i < names.size(); ++i) {
            checkAddResponse(names.get(i), prices.get(i), client);
            checkGetResponse(constructExpectedBody(names.subList(0, i + 1), prices.subList(0, i + 1)), client);
        }
    }

    @Test
    public void testDoubleProducts() throws Exception {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("potatoes", "potatoes", "potatoes", "potatoes"));
        ArrayList<Long> prices = new ArrayList<>(Arrays.asList(200L, 400L, 200L, 200L));
        assertEquals(names.size(), prices.size());
        Utils.clearDatabase();
        HttpClient client = Utils.getClient();
        for (int i = 0; i < names.size(); ++i) {
            checkAddResponse(names.get(i), prices.get(i), client);
            checkGetResponse(constructExpectedBody(names.subList(0, i + 1), prices.subList(0, i + 1)), client);
        }
    }

    @Test
    public void testAddProductWithIncorrectParameters() throws Exception {
        long price = 500L;
        HttpClient client = Utils.getClient();
        String uriString = Utils.HOST + Utils.SERVLETS_NAMES.ADD_PRODUCT.label;
        URI uri = new URIBuilder(uriString)
                .addParameter("price", Long.toString(price))
                .build();
        HttpResponse<String> response = Utils.getResponse(client, uri);
        assertEquals(200, response.statusCode());
        assertEquals("OK" + System.lineSeparator(), response.body());
        checkGetResponse(constructExpectedBody(List.of("null"), List.of(price)), client);

        uri = new URIBuilder(uriString)
                .addParameter("name", "potatos")
                .build();
        response = Utils.getResponse(Utils.getClient(), uri);
        assertEquals(500, response.statusCode());

        uri = new URIBuilder(uriString)
                .addParameter("name", "potatos")
                .addParameter("price", "not a number")
                .build();
        response = Utils.getResponse(Utils.getClient(), uri);
        assertEquals(500, response.statusCode());
    }

    private void checkGetResponse(String expectedBody, HttpClient client) throws Exception {
        String uriString = Utils.HOST + Utils.SERVLETS_NAMES.GET_PRODUCT.label;
        URI uri = new URIBuilder(uriString).build();
        HttpResponse<String> response = Utils.getResponse(client, uri);
        assertEquals(response.statusCode(), 200);
        assertEquals(expectedBody, response.body());
    }

    private void checkAddResponse(String name, Long price, HttpClient client) throws Exception {
        assertEquals("OK" + System.lineSeparator(), Utils.addProduct(name, price, client));
    }

    private String constructExpectedBody(List<String> names, List<Long> prices) {
        Assert.assertEquals(names.size(), prices.size());
        StringBuilder builder = new StringBuilder("<html><body>" + System.lineSeparator());
        for (int i = 0; i < names.size(); ++i) {
            builder.append(names.get(i)).append('\t').append(prices.get(i)).append("</br>").append(System.lineSeparator());
        }
        builder.append("</body></html>").append(System.lineSeparator());
        return builder.toString();
    }
}