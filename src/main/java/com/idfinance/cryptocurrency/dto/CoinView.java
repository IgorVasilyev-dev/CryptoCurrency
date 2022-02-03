package com.idfinance.cryptocurrency.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinView {

    private String name;

    private String symbol;

    private BigDecimal price_usd;

    private final DecimalFormat df = new DecimalFormat("0.00#######");

    public CoinView() {
    }

    public CoinView(String name, String symbol, BigDecimal price_usd) {
        this.name = name;
        this.symbol = symbol;
        this.price_usd = price_usd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice_usd() {
        return df.format(price_usd.stripTrailingZeros());
    }

    public void setPrice_usd(BigDecimal price_usd) {
        this.price_usd = price_usd;
    }

    @Override
    public String toString() {
        return "CoinView{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price_usd=" + price_usd +
                '}';
    }
}
