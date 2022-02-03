package com.idfinance.cryptocurrency.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class UserSubscription {

    private String userName;

    private String symbol;

    private String tokenName;

    private BigDecimal price;

    public UserSubscription(String userName, String symbol, String tokenName, BigDecimal price) {
        this.userName = userName;
        this.symbol = symbol;
        this.tokenName = tokenName;
        this.price = price;
    }

    public UserSubscription(String userName, String symbol) {
        this.userName = userName;
        this.symbol = symbol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSubscription that = (UserSubscription) o;
        return userName.equals(that.userName) && symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, symbol);
    }

    @Override
    public String toString() {
        return "UserSubscription{" +
                "userName='" + userName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", price=" + price +
                '}';
    }
}
