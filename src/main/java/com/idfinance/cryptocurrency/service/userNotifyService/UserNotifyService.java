package com.idfinance.cryptocurrency.service.userNotifyService;

import com.idfinance.cryptocurrency.dto.UserSubscription;
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
    private final static BigDecimal percentRate = new BigDecimal("0.01");
    private final static BigDecimal ONE_HUNDRED = new BigDecimal(100L);

    private final Map<String, HashSet<UserSubscriptionWithPrice>> cache = new ConcurrentHashMap<>();

    /**
     * Метод добавляет подписку на CryptoCoin по его symbol
     * @param userName имя подписчика
     * @param cryptoCoin coin на который подписываются
     */
    @Override
    public void addSubscription(String userName, CryptoCoin cryptoCoin) {
        UserSubscriptionWithPrice subscriptionWithPrice = new UserSubscriptionWithPrice(userName, cryptoCoin);
        String key = cryptoCoin.getSymbol();
        HashSet<UserSubscriptionWithPrice> userSubscriptionWithPrices;
        if(this.cache.containsKey(key)) {
            userSubscriptionWithPrices = cache.get(key);
            userSubscriptionWithPrices.add(subscriptionWithPrice);
        } else {
            userSubscriptionWithPrices = new HashSet<>();
            userSubscriptionWithPrices.add(subscriptionWithPrice);
            cache.put(key, userSubscriptionWithPrices);
        }
    }

    /**
     * Метод удаляет подписку на токен
     * @param subscription подписка
     */
    @Override
    public void deleteSubscription(UserSubscription subscription) {
        boolean removeFlag = false;
        String symbol = subscription.getSymbol();
        if(cache.containsKey(symbol)) {
            removeFlag = cache.get(symbol).remove(new UserSubscriptionWithPrice(subscription));
        }
        if(!removeFlag) {
            throw new RuntimeException(format("You are not subscribed to %s ", symbol));
        }
    }

    /**
     * Метод проверки стоимости токена к стоимости токена в подписке
     * Если стоимость изменилась на величину percentRate, в лог пишется сообщение уровня WARN
     * @param coin токен
     */
    @Override
    public void checkSubscription(CryptoCoin coin) {
        HashSet<UserSubscriptionWithPrice> userSubscriptionWithPrices = this.cache.get(coin.getSymbol());
        if(!userSubscriptionWithPrices.isEmpty()) {
            for (UserSubscriptionWithPrice subscription : userSubscriptionWithPrices) {
                BigDecimal delta = calculateDelta(subscription.getPrice(), coin.getUsd_price());
                boolean isChangeOnPercentRate = checkDeltaChangeOnPercentRate(delta);
                if(isChangeOnPercentRate) {
                    logUserNotification(subscription, delta);
                }
            }
        }
    }

    private boolean checkDeltaChangeOnPercentRate(BigDecimal delta) {
        return delta.compareTo(percentRate) >= 0;
    }

    private void logUserNotification(UserSubscriptionWithPrice subscription, BigDecimal delta) {
        log.log(Level.WARNING, format("Token = %s,User = %s, percent change = %s",
                subscription.getSymbol(),
                subscription.getUserName(),
                delta.multiply(ONE_HUNDRED)));
    }

    private BigDecimal calculateDelta(BigDecimal subscriptionPrice, BigDecimal coinPrice) {
        return subscriptionPrice.divide(coinPrice, 9, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).abs();
    }

}
