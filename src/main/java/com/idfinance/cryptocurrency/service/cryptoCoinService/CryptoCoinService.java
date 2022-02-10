package com.idfinance.cryptocurrency.service.cryptoCoinService;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.model.CryptoCoin;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import com.idfinance.cryptocurrency.service.userNotifyService.api.IUserNotifyService;
import com.idfinance.cryptocurrency.storage.api.ICryptoCoinRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class CryptoCoinService implements ICryptoCoinService {

    private final ICryptoCoinRepository repository;
    private final ICoinLoreResponseService coinLoreResponseService;
    private final IUserNotifyService userNotifyService;

    public CryptoCoinService(ICryptoCoinRepository repository, ICoinLoreResponseService coinLoreResponseService, IUserNotifyService userNotifyService) {
        this.repository = repository;
        this.coinLoreResponseService = coinLoreResponseService;
        this.userNotifyService = userNotifyService;
    }

    /**
     * Получить список доступных токенов из repository;
     * @return List<CoinView>
     */
    @Override
    public List<CoinView> getAll() {
        return this.repository.findAll().stream().map(e -> new CoinView(e.getName(), e.getSymbol(), e.getUsd_price())).collect(Collectors.toList());
    }

    /**
     * Получить токен из repository по его symbol;
     * @param symbol параметр запроса
     * @return токен
     */
    @Override
    public CoinView getCryptoCoinPrice(String symbol) {
        CryptoCoin cryptoCoin = this.repository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(format("CryptoCoin with symbol = %s Not Found", symbol)));
        return new CoinView(cryptoCoin.getName(), cryptoCoin.getSymbol(), cryptoCoin.getUsd_price());
    }

    /**
     * Добавить подписку на токен
     * @param request подписка
     */
    @Override
    public void addSubscription(CreateSubscriptionRequest request) {
        CryptoCoin cryptoCoin = this.repository.findBySymbol(request.getSymbol()).orElseThrow(
                () -> new RuntimeException(format("CryptoCoin with symbol = %s Not Found", request.getSymbol())));
        this.userNotifyService.addSubscription(request, cryptoCoin);
    }

    /**
     * Удалить подписку на токен
     * @param request подписка
     */
    @Override
    public void deleteSubscription(CreateSubscriptionRequest request) {
        this.userNotifyService.deleteSubscription(request);
    }

    /**
     * Метод асинхронной загрузки данных из удаленного источника в бд
     * Если цена токена изменилась, сохраняем изменения в бд и проверяем подписку
     */
    private void loadCoinsData() {
        for (Coin e:coinLoreResponseService.getAllAvailableCoins()) {
            CompletableFuture.supplyAsync(() -> this.coinLoreResponseService.getResponse(e.getId()))
                    .whenCompleteAsync((result, exp) -> {
                                if(exp == null) {
                                    try {
                                        CryptoCoin cryptoCoin;
                                        CoinView coinView = result.take();
                                        Optional<CryptoCoin> cryptoCoinOptional = this.repository.findBySymbol(coinView.getSymbol());
                                        if(cryptoCoinOptional.isPresent()) {
                                            cryptoCoin = cryptoCoinOptional.get();
                                            if(!cryptoCoin.getUsd_price().equals(coinView.getBigDecimalPrice_usd())) {
                                                cryptoCoin.setUsd_price(coinView.getBigDecimalPrice_usd());
                                                userNotifyService.checkPrice(this.repository.save(cryptoCoin));
                                            }
                                        } else {
                                            this.repository.save(
                                                    new CryptoCoin(coinView.getName(),
                                                            coinView.getSymbol(),
                                                            coinView.getBigDecimalPrice_usd()));
                                        }
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    exp.printStackTrace();
                                }
                            }
                    );
        }
    }

    /**
     * Метод запуска loadCoinsData с периодом(каждые 60 сек), запускается при полной загрузке приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadCoinsDataAfterStartup() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::loadCoinsData, 0, 1, TimeUnit.MINUTES);
    }

}
