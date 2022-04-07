package ru.akirakozov.sd.app.store;

public class SqlCompanyShares {
    String company;
    Integer shares_number;
    Integer shares_price;

    public SqlCompanyShares(){}

    public void setCompany(String company) {
        this.company = company;
    }

    public void setShares_number(Integer shares_number) {
        this.shares_number = shares_number;
    }

    public void setShares_price(Integer shares_price) {
        this.shares_price = shares_price;
    }

    public String getCompany() {
        return company;
    }

    public Integer getShares_number() {
        return shares_number;
    }

    public Integer getShares_price() {
        return shares_price;
    }
}
