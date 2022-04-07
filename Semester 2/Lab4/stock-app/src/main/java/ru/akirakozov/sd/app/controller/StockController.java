package ru.akirakozov.sd.app.controller;

import org.springframework.web.bind.annotation.*;
import ru.akirakozov.sd.app.errors.StockRuntimeException;
import ru.akirakozov.sd.app.store.StockStore;

@RestController
public class StockController {
    StockStore store;

    public StockController(StockStore store) {
        this.store = store;
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello /stock";
    }

    @RequestMapping(value = "/add-company", method = RequestMethod.GET)
    @ResponseBody
    public String addCompany(
            @RequestParam("company") String company,
            @RequestParam("number") int number,
            @RequestParam("price") int price
    ) {
        try {
            store.storeCompany(company, price);
            store.addShares(company, number);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/add-shares", method = RequestMethod.GET)
    @ResponseBody
    public String addShares(
            @RequestParam("company") String company,
            @RequestParam("number") int number
    ) {
        try {
            store.addShares(company, number);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/change-price", method = RequestMethod.GET)
    @ResponseBody
    public String changePrice(
            @RequestParam("company") String company,
            @RequestParam("price") int price
    ) {
        try {
            store.changeSharesPrice(company, price);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/shares-info", method = RequestMethod.GET)
    @ResponseBody
    public String sharesInfo(
            @RequestParam("company") String company
    ) {
        try {
            return "company: " + company + ", price: " + store.getSharesPrice(company) + ", number: " + store.getSharesNumber(company);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/shares-number", method = RequestMethod.GET)
    @ResponseBody
    public String sharesNumber(
            @RequestParam("company") String company
    ) {
        try {
            return String.valueOf(store.getSharesNumber(company));
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/shares-price", method = RequestMethod.GET)
    @ResponseBody
    public String sharesPrice(
            @RequestParam("company") String company
    ) {
        try {
            return String.valueOf(store.getSharesPrice(company));
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }


    @RequestMapping(value = "/buy-shares", method = RequestMethod.GET)
    @ResponseBody
    public String buyShares(
            @RequestParam("company") String company,
            @RequestParam("number") int number
    ) {
        try {
            if (store.buyShares(company, number)) {
                return "SUCCESS";
            }
            return "TO MANY SHARES NUMBER";
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/buy-shares-transaction", method = RequestMethod.GET)
    @ResponseBody
    public String buySharesTransaction(
            @RequestParam("company") String company,
            @RequestParam("sum") int sum,
            @RequestParam("number") int number
    ) {
       try {
            int sharesNumber = store.getSharesNumber(company);
            int sharesPrice = store.getSharesPrice(company);
            if (number >= sharesNumber) {
                return "Shares number in stock is less then required";
            }
            int totalPrice = sharesPrice * number;
            if (totalPrice > sum) {
                return "Not enough money to buy shares";
            }
            store.buyShares(company, number);
            return String.valueOf(sharesPrice * number);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/sell-shares-transaction", method = RequestMethod.GET)
    @ResponseBody
    public String sellSharesTransaction(
            @RequestParam("company") String company,
            @RequestParam("number") int number
    ) {
        try {
            int sharesPrice = store.getSharesPrice(company);
            store.addShares(company, number);
            return String.valueOf(sharesPrice * number);
        } catch (StockRuntimeException e) {
            return e.getMessage();
        }
    }


}
