package http;

import dao.*;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import models.Item;
import models.User;
import rx.Observable;

import java.util.*;
import java.util.stream.Collectors;

public class RxCatalogServer {
    private final UserDao userDao;
    private final CatalogDao catalogDao;

    public RxCatalogServer(UserDao userDao, CatalogDao catalogDao) {
        this.userDao = userDao;
        this.catalogDao = catalogDao;
    }

    public Observable<String> getResponse(HttpServerRequest<ByteBuf> req) {
        String path = req.getDecodedPath().substring(1);
        switch (path) {
            case "add_item":
                return addItem(req);
            case "register":
                return register(req);
            case "catalog":
                return showCatalog(req);
            default:
                return Observable.just("Unknown request: '" + path + "'.Known commands:\n" +
                        "add_item\n\tParameters: name, value, currency\n\tInfo: add item with given name into catalog with price in currency\n" +
                        "register\n\tParameters: name, id, currency\n\tInfo: register user with such name and id to display items in catalog in given currency\n" +
                        "catalog\n\tParameters: id\n\tInfo: shows items in catalog for given user in currency, that was selected while registration\n"
                );
        }
    }

    private Observable<String> showCatalog(HttpServerRequest<ByteBuf> req) {
        Optional<String> error = checkRequestParameters(req, List.of("id"));
        if (error.isPresent()) {
            return Observable.just(error.get());
        }
        Map<String, List<String>> queryParameters = req.getQueryParameters();
        int id = Integer.parseInt(queryParameters.get("id").get(0));

        Observable<Item> items = catalogDao.getItems();

        Observable<User> user = userDao.getUserById(id);
        return user.isEmpty().flatMap(
                isEmpty ->
                {
                    if (isEmpty) {
                        return Observable.just("No such user with id: " + id);
                    } else {
                        Observable<Item> currencyItems = user.flatMap(u -> items.map(item -> catalogDao.convertItemTo(item, u.getCurrency())));
                        return currencyItems.collect(StringBuilder::new, (sb, x) -> sb.append(x).append("\n")).map(StringBuilder::toString);
                    }
                }
        );
    }

    private Observable<String> register(HttpServerRequest<ByteBuf> req) {
        Optional<String> error = checkRequestParameters(req, Arrays.asList("id", "name", "currency"));
        if (error.isPresent()) {
            return Observable.just(error.get());
        }
        Map<String, List<String>> queryParameters = req.getQueryParameters();
        String name = queryParameters.get("name").get(0);
        int id = Integer.parseInt(queryParameters.get("id").get(0));
        String currency = getCurrency(queryParameters);
        return userDao.registerUser(id, currency, name).map(Objects::toString).onErrorReturn(Throwable::getMessage);
    }

    private Observable<String> addItem(HttpServerRequest<ByteBuf> req) {
        Optional<String> error = checkRequestParameters(req, Arrays.asList("name", "value", "currency"));
        if (error.isPresent()) {
            return Observable.just(error.get());
        }
        Map<String, List<String>> queryParameters = req.getQueryParameters();
        String name = queryParameters.get("name").get(0);
        int value = Integer.parseInt(queryParameters.get("value").get(0));
        String currency = getCurrency(queryParameters);
        return catalogDao.addItem(name, value, currency).map(Objects::toString).onErrorReturn(Throwable::getMessage);
    }

    private Optional<String> checkRequestParameters(HttpServerRequest<ByteBuf> req, List<String> parameters) {
        String no_parameters_error = parameters.stream().filter(x -> !req.getQueryParameters().containsKey(x)).collect(Collectors.joining(", "));
        return (no_parameters_error.isEmpty()) ? Optional.empty() : Optional.of("no parameters in request: " + no_parameters_error);
    }

    private String getCurrency(Map<String, List<String>> queryParameters) {
        return queryParameters.get("currency").get(0).toUpperCase();
    }

}
