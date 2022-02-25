package com.idfinance.cryptocurrency.service.userNotifyService.api;

import com.idfinance.cryptocurrency.dto.UserSubscription;
import com.idfinance.cryptocurrency.model.CryptoCoin;

public interface IUserNotifyService {

    /**
     * Подписаться на изменение цены coin
     * @param userName имя юзера
     * @param cryptoCoin coin подписки
     */
    void addSubscription(String userName, CryptoCoin cryptoCoin);

    /**
     * Удалить подписку на токен
     * @param userSubscription подписка
     */
    void deleteSubscription(UserSubscription userSubscription);

    /**
     * Проверить стоимость токена
     * @param coin токен
     */
    void checkSubscription(CryptoCoin coin);
}
