package ru.akirakozov.sd.refactoring;

import org.apache.http.client.utils.URIBuilder;
import org.junit.*;

import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTest extends BaseServerDBTest {

    @Test
    public void servletsResponse200() throws Exception {
        HttpClient client = Utils.getClient();
        for (Utils.SERVLETS_NAMES servlet : Utils.SERVLETS_NAMES.values()) {
            String uriString = Utils.HOST + servlet.label;
            URI uri;
            if (servlet.label.equals("add-product")) {
                uri = new URIBuilder(uriString)
                        .addParameter("name", "tomatoes")
                        .addParameter("price", "200")
                        .build();
            } else {
                uri = new URIBuilder(uriString)
                        .build();
            }
            HttpResponse<String> response = Utils.getResponse(client, uri);
            Assert.assertEquals("Response status for servlet " + servlet, 200, response.statusCode());
        }
    }
}