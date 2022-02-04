package com.idfinance.cryptocurrency.service.userNotifyService.api;

import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.model.CryptoCoin;

public interface IUserNotifyService {

    /**
     * Подписаться на изменения стоимости coin
     * @param request подписка
     * @param coin токен
     */
    void addSubscription(CreateSubscriptionRequest request, CryptoCoin coin);

    /**
     * Удалить подписку на токен
     * @param request подписка
     */
    void deleteSubscription(CreateSubscriptionRequest request);

    /**
     * Проверить стоимость токена
     * @param coin токен
     */
    void checkPrice(CryptoCoin coin);
}
