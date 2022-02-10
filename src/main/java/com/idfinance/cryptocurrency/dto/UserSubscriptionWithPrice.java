package com.idfinance.cryptocurrency.dto;

import com.idfinance.cryptocurrency.model.CryptoCoin;

import java.math.BigDecimal;

public class UserSubscriptionWithPrice extends UserSubscription {

    private String tokenName;

    private BigDecimal price;

    public UserSubscriptionWithPrice(String userName, String symbol, String tokenName, BigDecimal price) {
        super(userName, symbol);
        this.tokenName = tokenName;
        this.price = price;
    }

    public UserSubscriptionWithPrice(String userName, String symbol) {
        super(userName, symbol);
    }

    public UserSubscriptionWithPrice(String userName, CryptoCoin cryptoCoin) {
        super(userName, cryptoCoin.getSymbol());
        this.tokenName = cryptoCoin.getName();
        this.price = cryptoCoin.getUsd_price();
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
    public String toString() {
        return "UserSubscriptionWithPrice{" +
                "userName='" + getUserName() + '\'' +
                ", symbol='" + getSymbol() + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", price=" + price +
                '}';
    }
}
