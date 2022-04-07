package ru.akirakozov.sd.app;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Before run tests you should start Client Application
public class AppTest {

    @ClassRule
    public static GenericContainer stockServer
            = new FixedHostPortGenericContainer("stock-app:1.0-SNAPSHOT")
            .withFixedExposedPort(8081, 8081)
            .withExposedPorts(8081)
            .withAccessToHost(true);

    @ClassRule
    public static MySQLContainer mysql = new MySQLContainer("mysql:latest")
            .withDatabaseName("stock")
            .withUsername("any_username")
            .withPassword("any_passw");

    @BeforeClass
    public static void initDatabaseProperties() throws Exception {
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());


        assertRequest("http://localhost:8080/replace-memory-db", "SUCCESS");
        assertRequest("http://localhost:8081/add-company?company=Google&number=8000&price=1000", "SUCCESS");
        assertRequest("http://localhost:8081/add-company?company=Yandex&number=3000&price=100", "SUCCESS");
        assertRequest("http://localhost:8081/add-company?company=Bloomberg&number=2000&price=5000", "SUCCESS");
        assertRequest("http://localhost:8081/add-company?company=Rare&number=2&price=100", "SUCCESS");
    }


    @Test
    public void testStock() throws Exception {
        assertRequest("http://localhost:8081/hello", "hello /stock");
    }

    @Test
    public void testClient() throws Exception {
        assertRequest("http://localhost:8080/hello", "hello /stock");
    }

    @Test
    public void addClientTest() throws Exception {
        assertRequest("http://localhost:8080/add-client?id=add_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-client?id=add_client_2", "SUCCESS");
        assertRequest("http://localhost:8080/add-client?id=add_client_3", "SUCCESS");
        assertRequest("http://localhost:8080/add-client?id=add_client_1", "Already have such user");
    }

    @Test
    public void addMoneyClientTest() throws Exception {
        assertErrorRequest("http://localhost:8080/add-money", 400);
        assertErrorRequest("http://localhost:8080/add-money?id=not_such_client", 400);
        assertRequest("http://localhost:8080/add-client?id=add_money_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=add_money_client_1&money=100000", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=add_money_client_1&money=1000", "SUCCESS");
        assertRequest("http://localhost:8080/add-client?id=add_money_client_2", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=add_money_client_2&money=5000", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=not_such_client&money=5000", "No such user");
        assertRequest("http://localhost:8080/add-money?id=not_such_client&money=200", "No such user");
    }

    @Test
    public void buyShares() throws Exception {
        assertRequest("http://localhost:8080/add-client?id=buy_shares_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=buy_shares_client_1&money=100000", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=buy_shares_client_1&company=Google&number=3", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=buy_shares_client_1&company=Google&number=300", "Not enough money to buy shares");
        assertRequest("http://localhost:8080/buy-shares?id=buy_shares_client_1&company=Rare&number=100", "Shares number in stock is less then required");
    }

    @Test
    public void getShares() throws Exception {
        assertRequest("http://localhost:8080/add-client?id=get_shares_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=get_shares_client_1&money=100000", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=get_shares_client_1&company=Google&number=3", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=get_shares_client_1&company=Rare&number=1", "SUCCESS");
        assertRequest("http://localhost:8080/get-shares?id=get_shares_client_1",
                "Shares of company: Google, number 3\n" +
                "Shares of company: Rare, number 1");

        assertRequest("http://localhost:8080/add-client?id=get_shares_client_2", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=get_shares_client_2&money=100", "SUCCESS");
        assertRequest("http://localhost:8080/get-shares?id=get_shares_client_2", "");
    }


    @Test
    public void getBalance() throws Exception {
        assertRequest("http://localhost:8080/add-client?id=get_balance_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=get_balance_client_1&money=100000", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=get_balance_client_1&company=Google&number=3", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=get_balance_client_1", "100000");
        assertRequest("http://localhost:8081/change-price?company=Google&price=500", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=get_balance_client_1", "98500");
        assertRequest("http://localhost:8081/change-price?company=Google&price=1000", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=get_balance_client_1", "100000");
    }


    @Test
    public void sellShares() throws Exception {
        assertRequest("http://localhost:8080/add-client?id=sell_shares_client_1", "SUCCESS");
        assertRequest("http://localhost:8080/add-money?id=sell_shares_client_1&money=100000", "SUCCESS");
        assertRequest("http://localhost:8080/buy-shares?id=sell_shares_client_1&company=Google&number=3", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=sell_shares_client_1", "100000");
        assertRequest("http://localhost:8081/change-price?company=Google&price=500", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=sell_shares_client_1", "98500");
        assertRequest("http://localhost:8080/sell-shares?id=sell_shares_client_1&company=Google&number=2", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=sell_shares_client_1", "98500");
        assertRequest("http://localhost:8081/change-price?company=Google&price=1000", "SUCCESS");
        assertRequest("http://localhost:8080/get-balance?id=sell_shares_client_1", "99000");
        assertRequest("http://localhost:8080/sell-shares?id=sell_shares_client_1&company=Google&number=2", "Not enough shares to sell");
    }

    private static void assertErrorRequest(String query, int error) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(query))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(response.statusCode(), error);
    }

    private static void assertRequest(String query, String expectedResult) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(query))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(expectedResult, response.body());
    }
}
