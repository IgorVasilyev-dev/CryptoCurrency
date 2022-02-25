package com.idfinance.cryptocurrency.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UserSubscription {

    @NotBlank
    private final String userName;

    @NotBlank
    private final String symbol;

    public UserSubscription(String userName, String symbol) {
        this.userName = userName;
        this.symbol = symbol;
    }

    public String getUserName() {
        return userName;
    }

    public String getSymbol() {
        return symbol;
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
                '}';
    }
}
