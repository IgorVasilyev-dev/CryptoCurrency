package com.idfinance.cryptocurrency.service.cryptoCoinService.api;

import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;

import java.util.List;

public interface ICryptoCoinService {

    List<CoinView> getAll();

    CoinView getCryptoCoinPrice(String symbol);

    void addSubscription(CreateSubscriptionRequest request);

    void deleteSubscription(CreateSubscriptionRequest request);
}
