package com.idfinance.cryptocurrency.service.cryptoCoinService.api;

import com.idfinance.cryptocurrency.dto.CoinView;

import java.util.List;

public interface ICryptoCoinService {

    List<CoinView> getAll();

    CoinView getCryptoCoinPrice(String symbol);
}
