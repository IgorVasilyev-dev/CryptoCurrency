package com.idfinance.cryptocurrency.dto;

import com.idfinance.cryptocurrency.model.CryptoCoin;

import java.math.BigDecimal;

public class UserSubscriptionWithPrice extends UserSubscription {

    private BigDecimal price;

    public UserSubscriptionWithPrice(String userName, String symbol) {
        super(userName, symbol);
    }

    public UserSubscriptionWithPrice(UserSubscription userSubscription) {
        super(
                userSubscription.getUserName(),
                userSubscription.getSymbol()
        );
    }

    public UserSubscriptionWithPrice(String userName, CryptoCoin cryptoCoin) {
        super(userName, cryptoCoin.getSymbol());
        this.price = cryptoCoin.getUsd_price();
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
                ", price=" + price +
                '}';
    }
}
