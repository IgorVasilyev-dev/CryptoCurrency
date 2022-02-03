package com.idfinance.cryptocurrency.dto;

import javax.validation.constraints.NotBlank;

public class CreateSubscriptionRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String symbol;

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

    @Override
    public String toString() {
        return "CreateSubscriptionRequest{" +
                "userName='" + userName + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
