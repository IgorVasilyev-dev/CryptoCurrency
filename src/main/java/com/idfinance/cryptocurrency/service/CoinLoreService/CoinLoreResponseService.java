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
    private final BlockingQueue<CoinView> coinViewBlockingQueue;

    public CoinLoreResponseService(RestTemplate restTemplate, CoinLoreRequestProperty coinLoreRequestProperty) {
        this.restTemplate = restTemplate;
        this.coinLoreRequestProperty = coinLoreRequestProperty;
        this.coinViewBlockingQueue = new ArrayBlockingQueue<>(coinLoreRequestProperty.getCoinList().size());
    }


    /**
     * Получить список доступных монет из конфигурации CoinLoreRequestProperty
     * @return список доступных монет
     */
    @Override
    public List<Coin> getAllAvailableCoins() {
        return this.coinLoreRequestProperty.getCoinList();
    }

    /**
     * Получить данные с удаленного api
     * @param id токена
     * @return блокирующая очередь потоков, очередь ограничена количеством токенов в конфигурации CoinLoreRequestProperty
     */
    @Override
    public BlockingQueue<CoinView> getResponse(Long id) {
        this.coinViewBlockingQueue.add(Objects.requireNonNull(
                restTemplate.getForObject((coinLoreRequestProperty.getBaseUrl() + "/?id=" + id), CoinView[].class))[0]);
        return this.coinViewBlockingQueue;
    }

}
