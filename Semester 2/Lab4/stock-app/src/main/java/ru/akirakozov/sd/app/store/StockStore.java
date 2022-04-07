package ru.akirakozov.sd.app.store;

public interface StockStore {

    void storeCompany(String companyName, int sharesPrice);

    void addShares(String companyName, int sharesNumber);

    int getSharesNumber(String companyName);

    int getSharesPrice(String companyName);

    boolean buyShares(String companyName, int sharesNumber);

    void changeSharesPrice(String companyName, int newPrice);

}
