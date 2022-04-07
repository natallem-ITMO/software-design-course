package ru.akirakozov.sd.app.model;

public class User {
    String id;
    Integer money;

    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public Integer getMoney() {
        return money;
    }

    public User() {
    }
}
