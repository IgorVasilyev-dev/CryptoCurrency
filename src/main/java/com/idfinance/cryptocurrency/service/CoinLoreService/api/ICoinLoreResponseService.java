package com.idfinance.cryptocurrency.service.CoinLoreService.api;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface ICoinLoreResponseService {

    List<Coin> getAllAvailableCoins();

    BlockingQueue<CoinView> getResponse(String url);

}
