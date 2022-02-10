package com.idfinance.cryptocurrency.service.userNotifyService;

import com.idfinance.cryptocurrency.dto.UserSubscriptionWithPrice;
import com.idfinance.cryptocurrency.model.CryptoCoin;
import com.idfinance.cryptocurrency.service.userNotifyService.api.IUserNotifyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class UserNotifyService implements IUserNotifyService {

    private static final Logger log = Logger.getLogger(UserNotifyService.class.getName());
    private final Map<String, HashSet<UserSubscriptionWithPrice>> cache = new ConcurrentHashMap<>();
    private final BigDecimal percentRate = new BigDecimal("0.00001");

    /**
     * Метод добавляет подписку на coin
     * @param userSubscriptionWithPrice подписка
     */
    @Override
    public void addSubscription(UserSubscriptionWithPrice userSubscriptionWithPrice) {
        String key = userSubscriptionWithPrice.getSymbol();
        HashSet<UserSubscriptionWithPrice> userSubscriptionWithPrices;
        if(this.cache.containsKey(key)) {
            userSubscriptionWithPrices = cache.get(key);
            userSubscriptionWithPrices.add(userSubscriptionWithPrice);
        } else {
            userSubscriptionWithPrices = new HashSet<>();
            userSubscriptionWithPrices.add(userSubscriptionWithPrice);
            cache.put(key, userSubscriptionWithPrices);
        }
    }

    /**
     * Метод удаляет подписку на токен
     * @param subscription подписка
     */
    @Override
    public void deleteSubscription(UserSubscriptionWithPrice subscription) {
        boolean removeFlag = false;
        if(cache.containsKey(subscription.getSymbol())) {
            removeFlag = cache.get(subscription.getSymbol()).remove(subscription);
        }
        if(!removeFlag) {
            throw new RuntimeException(format("You are not subscribed to %s ", subscription.getSymbol()));
        }
    }

    /**
     * Метод проверки стоимости токена к стоимости токена в подписке
     * Если стоимость изменилась на 1%, в лог пишется сообщение уровня WARN
     * @param coin токен
     */
    @Override
    public void checkPrice(CryptoCoin coin) {
        HashSet<UserSubscriptionWithPrice> userSubscriptionWithPrices = this.cache.get(coin.getSymbol());
        if(!userSubscriptionWithPrices.isEmpty()) {
            for (UserSubscriptionWithPrice userSubscriptionWithPrice : userSubscriptionWithPrices) {
                BigDecimal delta = userSubscriptionWithPrice.getPrice().
                        divide(coin.getUsd_price(), 9, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).abs();
                if((delta.compareTo(percentRate) >= 0)) {
                    log.log(Level.WARNING, format("Token = %s,User = %s, percent change = %s",
                            userSubscriptionWithPrice.getSymbol(),
                            userSubscriptionWithPrice.getUserName(),
                            delta.multiply(BigDecimal.valueOf(100L))));
                }
            }
        }
    }

}
