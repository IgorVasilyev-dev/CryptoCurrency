package com.idfinance.cryptocurrency.service.CoinLoreService;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.idfinance.cryptocurrency.dto.Coin;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinLoreRequestProperty {

    @Value("${coinLore.url}")
    private String baseUrl;

    @Value("${coinLore.available_coins}")
    private String resourcesPath;

    private final List<Coin> coinList = new ArrayList<>();

    /**
     * Загрузка данных в coinList из файла(json) после инициализации
     */
    @PostConstruct
    private void init() {
        File file = new File(resourcesPath);
        if (file.exists()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Coin[] coins = objectMapper.readValue(file, Coin[].class);
                this.coinList.addAll(Arrays.asList(coins));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CoinLoreRequestProperty() {
    }

    public CoinLoreRequestProperty(String baseUrl, String resourcesUrl) {
        this.baseUrl = baseUrl;
        this.resourcesPath = resourcesUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public List<Coin> getCoinList() {
        return coinList;
    }

}
