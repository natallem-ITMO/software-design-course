package ru.akirakozov.sd.app.store;

import ru.akirakozov.sd.app.model.Shares;

import java.util.List;

public interface ClientStore {

    void createUser(String userId);

    void addMoney(String userId, int money);

    void reduceMoney(String userId, int money);

    List<Shares> getSharesOfUser(String userId);

    void buyShares(String userId, String companyName, int sharesNumber);

    void sellShares(String userId, String companyName, int sharesNumber);

    int getBalance(String userId);
}
