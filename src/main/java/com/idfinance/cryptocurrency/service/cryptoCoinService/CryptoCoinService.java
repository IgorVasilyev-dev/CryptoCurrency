package com.idfinance.cryptocurrency.service.cryptoCoinService;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.dto.UserSubscription;
import com.idfinance.cryptocurrency.dto.UserSubscriptionWithPrice;
import com.idfinance.cryptocurrency.model.CryptoCoin;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import com.idfinance.cryptocurrency.service.userNotifyService.api.IUserNotifyService;
import com.idfinance.cryptocurrency.storage.api.ICryptoCoinRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
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
        return this.repository.findAll().stream().map(CoinView::new).collect(Collectors.toList());
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
        return new CoinView(cryptoCoin);
    }

    /**
     * Добавить подписку на токен
     * @param request подписка
     */
    @Override
    public void addSubscription(UserSubscription request) {
        CryptoCoin cryptoCoin = this.repository.findBySymbol(request.getSymbol()).orElseThrow(
                () -> new RuntimeException(format("CryptoCoin with symbol = %s Not Found", request.getSymbol())));
        this.userNotifyService.addSubscription(new UserSubscriptionWithPrice(
                request.getUserName(), cryptoCoin));
    }

    /**
     * Удалить подписку на токен
     * @param request подписка
     */
    @Override
    public void deleteSubscription(UserSubscription request) {
        this.userNotifyService.deleteSubscription(new UserSubscriptionWithPrice(
                request.getUserName(), request.getSymbol()));
    }

    /**
     * Метод асинхронной загрузки данных из удаленного источника в бд
     */
    private void asyncLoadCoinsData() {
        for (Coin coin:coinLoreResponseService.getAllAvailableCoins()) {
            CompletableFuture.supplyAsync(() -> this.coinLoreResponseService.getResponse(coin.getId()))
                    .whenCompleteAsync((result, exception) -> checkPriceAndSaveChange(result));
        }
    }

    /**
     * Метод проверки цены токена и сохранения в бд
     * Если цена токена изменилась, сохраняем изменения в бд и проверяем подписку
     * @param result BlockingQueue<CoinView>
     */
    private void checkPriceAndSaveChange(BlockingQueue<Optional<CoinView>> result) {
        Optional<CoinView> optionalCoinView = getOptionalResult(result);
        if(optionalCoinView.isPresent()) {
            CoinView coinView = optionalCoinView.get();
            CryptoCoin cryptoCoin = this.repository.findBySymbol(coinView.getSymbol())
                    .orElseGet(() -> this.repository.save(new CryptoCoin(coinView)));
            boolean isPriceChange = isPriceChange(coinView, cryptoCoin);
            if(isPriceChange) {
                saveChange(coinView, cryptoCoin);
                checkSubscription(coinView);
            }
        }
    }

    private void saveChange(CoinView coinView, CryptoCoin cryptoCoin) {
        cryptoCoin.setUsd_price(coinView.getBigDecimalPrice_usd());
        this.repository.save(cryptoCoin);
    }

    private void checkSubscription(CoinView coinView) {
        this.userNotifyService.checkSubscription(new CryptoCoin(coinView));
    }

    private Optional<CoinView> getOptionalResult(BlockingQueue<Optional<CoinView>> result) {
        Optional<CoinView> optionalCoinView = Optional.empty();
        try {
            optionalCoinView = result.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return optionalCoinView;
    }

    private boolean isPriceChange(CoinView coinView, CryptoCoin cryptoCoin) {
        return coinView.getBigDecimalPrice_usd().compareTo(cryptoCoin.getUsd_price()) != 0;
    }

    /**
     * Метод запуска asyncLoadCoinsData с периодом(каждые 60 сек), запускается при полной загрузке приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadCoinsDataAfterStartup() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::asyncLoadCoinsData, 0, 1, TimeUnit.MINUTES);
    }

}
