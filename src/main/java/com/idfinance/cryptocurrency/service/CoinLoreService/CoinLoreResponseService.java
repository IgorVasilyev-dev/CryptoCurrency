package com.idfinance.cryptocurrency.service.CoinLoreService;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CoinLoreResponseService implements ICoinLoreResponseService {

    private final RestTemplate restTemplate;
    private final CoinLoreRequestProperty coinLoreRequestProperty;
    BlockingQueue<CoinView> ff = new ArrayBlockingQueue<>(3, true);

    public CoinLoreResponseService(RestTemplate restTemplate, CoinLoreRequestProperty coinLoreRequestProperty) {
        this.restTemplate = restTemplate;
        this.coinLoreRequestProperty = coinLoreRequestProperty;
    }


    @Override
    public List<Coin> getAllAvailableCoins() {
        return this.coinLoreRequestProperty.getCoinList();
    }

    @Override
    public BlockingQueue<CoinView> getResponse(String url) {
        this.ff.add(Objects.requireNonNull(restTemplate.getForObject(url, CoinView[].class))[0]);
        return this.ff;
    }

}
