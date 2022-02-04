package com.idfinance.cryptocurrency.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "crypto_coins")
public class CryptoCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name",columnDefinition = "varchar(25)", nullable = false, unique = true)
    private String name;

    @Column(name = "symbol",columnDefinition = "varchar(25)", nullable = false, unique = true)
    private String symbol;

    @Column(name = "price",columnDefinition = "numeric", precision = 21, scale = 9, nullable = false)
    private BigDecimal usd_price;

    public CryptoCoin() {
    }

    public CryptoCoin(String name, String symbol, BigDecimal usd_price) {
        this.name = name;
        this.symbol = symbol;
        this.usd_price = usd_price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getUsd_price() {
        return usd_price;
    }

    public void setUsd_price(BigDecimal usd_price) {
        this.usd_price = usd_price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoCoin that = (CryptoCoin) o;
        return name.equals(that.name) && symbol.equals(that.symbol) && usd_price.equals(that.usd_price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol, usd_price);
    }

    @Override
    public String toString() {
        return "CryptoCoin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", usd_price=" + usd_price +
                '}';
    }
}
