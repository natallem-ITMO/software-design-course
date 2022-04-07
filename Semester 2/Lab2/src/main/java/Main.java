import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import dao.CatalogDao;
import util.Converter;
import util.Currency;
import dao.UserDao;
import rx.Observable;
//import com.mongodb.async.client.Observable;
import http.RxCatalogServer;
import io.reactivex.netty.protocol.http.server.HttpServer;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        Converter converter = new Converter(createExampleCurrencyMap());
        UserDao userDao = new UserDao(client);
        CatalogDao catalogDao = new CatalogDao(client, converter);
        RxCatalogServer server = new RxCatalogServer(userDao, catalogDao);
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    Observable<String> response = server.getResponse(req);
                    return resp.writeString(response.map(s -> s + System.lineSeparator()));
                })
                .awaitShutdown();
    }

    static Map<Currency, Integer> createExampleCurrencyMap(){
        Map<Currency, Integer> currencyMap = new HashMap<>();
        currencyMap.put(Currency.DOLLARS, 1000);
        currencyMap.put(Currency.EUROS, 1300);
        currencyMap.put(Currency.RUBLES, 100);
        return currencyMap;
    }
}