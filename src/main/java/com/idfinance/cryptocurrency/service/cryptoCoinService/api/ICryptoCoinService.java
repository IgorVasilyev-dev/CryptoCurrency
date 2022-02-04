package com.idfinance.cryptocurrency.service.cryptoCoinService.api;

import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;

import java.util.List;

public interface ICryptoCoinService {

    /**
     * Получить все токены
     * @return List<CoinView>
     */
    List<CoinView> getAll();

    /**
     * Получить токен(имя, символ, цена) по symbol
     * @param symbol параметр запроса
     * @return CoinView
     */
    CoinView getCryptoCoinPrice(String symbol);

    /**
     * Добавить подписку на токен
     * @param request подписка
     */
    void addSubscription(CreateSubscriptionRequest request);

    /**
     * Удалить подписку на токен
     * @param request подписка
     */
    void deleteSubscription(CreateSubscriptionRequest request);
}
