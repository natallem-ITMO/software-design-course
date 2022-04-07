package ru.akirakozov.sd.app.store;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import ru.akirakozov.sd.app.errors.ClientRuntimeException;
import ru.akirakozov.sd.app.model.Shares;
import ru.akirakozov.sd.app.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryClientStore implements ClientStore {
    class UserInfo {
        User user;
        Map<String, Shares> shares = new HashMap<>();

        public UserInfo(String userId) {
            this.user = new User();
            user.setId(userId);
            user.setMoney(0);
        }
    }

    Map<String, UserInfo> users = new HashMap<>();

    @Override
    public void createUser(String userId) {
        if (!existsUser(userId)) {
            users.put(userId, new UserInfo(userId));
        } else {
            throw new ClientRuntimeException("Already have such user");
        }
    }

    @Override
    public void addMoney(String userId, int money) {
        User userInfo = getUserNotNull(userId);
        userInfo.setMoney(userInfo.getMoney() + money);
    }

    @Override
    public void reduceMoney(String userId, int money) {
        User userInfo = getUserNotNull(userId);
        userInfo.setMoney(userInfo.getMoney() - money);
    }

    @Override
    public List<Shares> getSharesOfUser(String userId) {
        User userInfo = getUserNotNull(userId);
        List<Shares> collect = new ArrayList<>(users.get(userId).shares.values());
        return collect;
    }

    @Override
    public void buyShares(String userId, String companyName, int sharesNumber) {
        getUserNotNull(userId);
        users.get(userId).shares.putIfAbsent(companyName, new Shares(userId, companyName, 0));
        Integer number = users.get(userId).shares.get(companyName).getNumber();
        users.get(userId).shares.get(companyName).setNumber(number + sharesNumber);
    }

    @Override
    public void sellShares(String userId, String companyName, int sharesNumber) {
        Shares shares = getUserShares(userId, companyName);
        if (shares == null || shares.getNumber() < sharesNumber) {
            throw new ClientRuntimeException("Not enough shares to sell");
        } else {
            Integer number = users.get(userId).shares.get(companyName).getNumber();
            users.get(userId).shares.get(companyName).setNumber(number - sharesNumber);
        }
    }

    @Override
    public int getBalance(String userId) {
        return getUserNotNull(userId).getMoney();
    }

    private Shares getUserShares(String userId, String companyName) {
        getUserNotNull(userId);
        return users.get(userId).shares.getOrDefault(companyName, null);
    }

    private User getUserNotNull(String userId) {
        User user = getUser(userId);
        if (user == null) {
            throw new ClientRuntimeException("No such user");
        }
        return user;
    }

    private boolean existsUser(String userId) {
        return getUser(userId) != null;
    }

    private User getUser(String userId) {
        if (users.containsKey(userId)) {
            return users.getOrDefault(userId, null).user;
        }
        return null;
    }
}
