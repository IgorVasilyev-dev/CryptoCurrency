package com.idfinance.cryptocurrency.service.userNotifyService.api;

import com.idfinance.cryptocurrency.dto.UserSubscriptionWithPrice;
import com.idfinance.cryptocurrency.model.CryptoCoin;

public interface IUserNotifyService {

    /**
     * Подписаться на изменения стоимости coin
     * @param userSubscriptionWithPrice подписка
     */
    void addSubscription(UserSubscriptionWithPrice userSubscriptionWithPrice);

    /**
     * Удалить подписку на токен
     * @param userSubscriptionWithPrice подписка
     */
    void deleteSubscription(UserSubscriptionWithPrice userSubscriptionWithPrice);

    /**
     * Проверить стоимость токена
     * @param coin токен
     */
    void checkPrice(CryptoCoin coin);
}
