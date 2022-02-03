package com.idfinance.cryptocurrency.dto;

public class Coin {

    private Long id;

    private String symbol;

    public Coin() {
    }

    public Coin(Long id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
