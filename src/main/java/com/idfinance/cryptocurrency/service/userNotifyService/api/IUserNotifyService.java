package com.idfinance.cryptocurrency.service.userNotifyService.api;

import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.model.CryptoCoin;

public interface IUserNotifyService {

    void addSubscription(CreateSubscriptionRequest request, CryptoCoin coin);

    void deleteSubscription(CreateSubscriptionRequest request);

    void checkPrice(CryptoCoin coin);
}
