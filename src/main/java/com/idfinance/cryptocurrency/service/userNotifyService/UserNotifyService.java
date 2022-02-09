package com.idfinance.cryptocurrency.service.userNotifyService;

import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.dto.UserSubscription;
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
    private final Map<String, HashSet<UserSubscription>> cache = new ConcurrentHashMap<>();
    private final BigDecimal percentRate = new BigDecimal("0.01");

    /**
     * Метод добавляет подписку на coin
     * @param request подписка
     * @param coin токен
     */
    @Override
    public void addSubscription(CreateSubscriptionRequest request, CryptoCoin coin) {
        if(cache.containsKey(coin.getSymbol())) {
            cache.get(coin.getSymbol()).add(new UserSubscription(
                    request.getUserName(), coin.getSymbol(), coin.getName(), coin.getUsd_price())
            );
        } else {
            HashSet<UserSubscription> userSubscriptions = new HashSet<>();
            userSubscriptions.add(new UserSubscription(
                    request.getUserName(), coin.getSymbol(), coin.getName(), coin.getUsd_price())
            );
            cache.put(coin.getSymbol(), userSubscriptions);
        }
    }

    /**
     * Метод удаляет подписку на токен
     * @param request подписка
     */
    @Override
    public void deleteSubscription(CreateSubscriptionRequest request) {
        if(cache.containsKey(request.getSymbol())) {
            cache.get(request.getSymbol()).remove(new UserSubscription(request.getUserName(), request.getSymbol()));
        } else {
            throw new RuntimeException(format("You are not subscribed to %s ", request.getSymbol()));
        }
    }

    /**
     * Метод проверки стоимости токена к стоимости токена в подписке
     * Если стоимость изменилась на 1%, в лог пишется сообщение уровня WARN
     * @param coin токен
     */
    @Override
    public void checkPrice(CryptoCoin coin) {
        for (Map.Entry<String, HashSet<UserSubscription>> e: this.cache.entrySet()) {
            if(e.getKey().equals(coin.getSymbol())) {
                for (UserSubscription userSubscription: e.getValue()) {
                    BigDecimal delta = userSubscription.getPrice().
                            divide(coin.getUsd_price(), 9, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).abs();
                    if((delta.compareTo(percentRate) >= 0)) {
                        log.log(Level.WARNING, format("Token = %s,User = %s, percent change = %s",
                                userSubscription.getSymbol(),
                                userSubscription.getUserName(),
                                delta.multiply(BigDecimal.valueOf(100L))));
                    }
                }
            }
        }
    }

}
