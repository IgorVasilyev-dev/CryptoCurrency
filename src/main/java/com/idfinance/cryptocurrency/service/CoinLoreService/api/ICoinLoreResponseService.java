package com.idfinance.cryptocurrency.service.CoinLoreService.api;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface ICoinLoreResponseService {

    /**
     * Получить список доступных монет
     * @return List<Coin>
     */
    List<Coin> getAllAvailableCoins();

    /**
     * Получить данные с удаленного api
     * @param id токена
     * @return BlockingQueue<CoinView>
     */
    BlockingQueue<CoinView> getResponse(Long id);

}
