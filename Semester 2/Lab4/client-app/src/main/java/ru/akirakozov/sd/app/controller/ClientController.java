package ru.akirakozov.sd.app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.akirakozov.sd.app.errors.ClientRuntimeException;
import ru.akirakozov.sd.app.model.Shares;
import ru.akirakozov.sd.app.store.ClientStore;
import ru.akirakozov.sd.app.store.MemoryClientStore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    ClientStore store;

    public ClientController(ClientStore store) {
        this.store = store;
    }

    @RequestMapping("/replace-memory-db")
    public String replaceMemoryDb() {
        if (store instanceof MemoryClientStore){
            store = new MemoryClientStore();
            return "SUCCESS";
        }
        return "Not memory db";
    }

    @RequestMapping("/hello")
    public String hello() {
        final String uri = "http://localhost:8081/hello";
        RestTemplate restTemplate = new RestTemplate();
        String priceString = restTemplate.getForObject(uri, String.class);
        return priceString;
    }

    @RequestMapping(value = "/add-client", method = RequestMethod.GET)
    @ResponseBody
    public String addClient(
            @RequestParam("id") String id
    ) {
        try {
            store.createUser(id);
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/add-money", method = RequestMethod.GET)
    @ResponseBody
    public String addMoney(
            @RequestParam("id") String id,
            @RequestParam("money") Integer money
    ) {
        try {
            store.addMoney(id, money);
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/get-shares", method = RequestMethod.GET)
    @ResponseBody
    public String getShares(
            @RequestParam("id") String id
    ) {
        try {
            List<Shares> sharesOfUser = store.getSharesOfUser(id);
            return sharesOfUser.stream().map(Object::toString).collect(Collectors.joining("\n"));
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/get-balance", method = RequestMethod.GET)
    @ResponseBody
    public String getBalance(
            @RequestParam("id") String id
    ) {
        try {
            int balance = store.getBalance(id);
            List<Shares> sharesOfUser = store.getSharesOfUser(id);
            Integer sharesSum = sharesOfUser.stream().map(shares ->
            {
                final String uri = "http://localhost:8081/shares-price?company=" + shares.getCompany();
                RestTemplate restTemplate = new RestTemplate();
                String priceString = restTemplate.getForObject(uri, String.class);
                int price = Integer.parseInt(priceString);
                return price * shares.getNumber();
            }).reduce(0, Integer::sum);
            return String.valueOf(sharesSum + balance);
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        } catch (Throwable th) {
            return "error occurred";
        }
    }

    @RequestMapping(value = "/buy-shares", method = RequestMethod.GET)
    @ResponseBody
    public String buyShares(
            @RequestParam("id") String id,
            @RequestParam("company") String company,
            @RequestParam("number") Integer number
    ) {
        try {
            int balance = store.getBalance(id);
            final String uri = String.format("http://localhost:8081/buy-shares-transaction?company=%s&sum=%d&number=%d", company, balance, number);
            RestTemplate restTemplate = new RestTemplate();
            String priceString = restTemplate.getForObject(uri, String.class);
            if (priceString == null) {
                return "Cannot access stock-market";
            }
            try {
                int price = Integer.parseInt(priceString);
                store.buyShares(id, company, number);
                store.reduceMoney(id, price);
                return "SUCCESS";
            } catch (NumberFormatException nfe) {
                return priceString;
            }
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/sell-shares", method = RequestMethod.GET)
    @ResponseBody
    public String sellShares(
            @RequestParam("id") String id,
            @RequestParam("company") String company,
            @RequestParam("number") Integer number
    ) {
        try {
            if (!checkEnoughShares(id, number, company)) {
                return "Not enough shares to sell";
            }
            final String uri = String.format("http://localhost:8081/sell-shares-transaction?company=%s&number=%d", company, number);
            RestTemplate restTemplate = new RestTemplate();
            String priceString = restTemplate.getForObject(uri, String.class);
            if (priceString == null) {
                return "Cannot access stock-market";
            }
            try {
                int price = Integer.parseInt(priceString);
                store.sellShares(id, company, number);
                store.addMoney(id, price);
                return "SUCCESS";
            } catch (NumberFormatException nfe) {
                return priceString;
            }
        } catch (ClientRuntimeException e) {
            return e.getMessage();
        }
    }

    private boolean checkEnoughShares(String userId, Integer number, String company) {
        List<Shares> sharesOfUser = store.getSharesOfUser(userId);
        int sharesNumber = 0;
        for (Shares shares : sharesOfUser) {
            if (shares.getCompany().equals(company)) {
                sharesNumber = shares.getNumber();
                break;
            }
        }
        return sharesNumber >= number;
    }

}
