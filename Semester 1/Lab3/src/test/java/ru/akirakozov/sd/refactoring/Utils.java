package ru.akirakozov.sd.refactoring;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class Utils {
    public static final String HOST = "http://localhost:8081/";

    public enum SERVLETS_NAMES {
        ADD_PRODUCT("add-product"),
        GET_PRODUCT("get-products"),
        QUERY("query");
        public final String label;
        SERVLETS_NAMES(String label) {
            this.label = label;
        }
    }

    public static HttpResponse<String> getResponse(HttpClient client, URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void clearDatabase() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public static HttpClient getClient(){
        return HttpClient.newHttpClient();
    }

    public static String getProducts(String name, Long price, HttpClient client) throws Exception {
        String uriString = Utils.HOST + Utils.SERVLETS_NAMES.ADD_PRODUCT.label;
        URI uri = new URIBuilder(uriString)
                .addParameter("name", name)
                .addParameter("price", Long.toString(price))
                .build();
        HttpResponse<String> response = Utils.getResponse(client, uri);
        assertEquals(200, response.statusCode());
        return response.body();
    }

    public static String addProduct(String name, Long price, HttpClient client) throws Exception {
        String uriString = Utils.HOST + Utils.SERVLETS_NAMES.ADD_PRODUCT.label;
        URI uri = new URIBuilder(uriString)
                .addParameter("name", name)
                .addParameter("price", Long.toString(price))
                .build();
        HttpResponse<String> response = Utils.getResponse(client, uri);
        assertEquals(200, response.statusCode());
        return response.body();
    }

}
