package ru.akirakozov.sd.app.model;

public class Shares {
    String user_id;
    String company;
    Integer number;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCompany() {
        return company;
    }

    public Integer getNumber() {
        return number;
    }

    public Shares() {
    }

    public Shares(String user_id, String company, Integer number) {
        this.user_id = user_id;
        this.company = company;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("Shares of company: %s, number %d", company, number);
    }
}
