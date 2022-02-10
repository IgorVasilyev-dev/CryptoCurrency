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
    private void loadCoinsData() {
        for (Coin e:coinLoreResponseService.getAllAvailableCoins()) {
            CompletableFuture.supplyAsync(() -> this.coinLoreResponseService.getResponse(e.getId()))
                    .whenCompleteAsync((result, exception) -> {
                        if(exception == null) {
                            this.checkAndSave(result);
                        } else {
                            exception.printStackTrace();
                        }
                    });
        }
    }

    /**
     * Метод проверки цены токена и сохранения в бд
     * Если цена токена изменилась, сохраняем изменения в бд и проверяем подписку
     * @param result BlockingQueue<CoinView>
     */
    private void checkAndSave(BlockingQueue<Optional<CoinView>> result) {
        Optional<CoinView> optionalCoinView = Optional.empty();
        try {
            optionalCoinView = result.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (optionalCoinView.isPresent()) {
            CoinView source = optionalCoinView.get();
            Optional<CryptoCoin> cryptoCoinOptional = this.repository.findBySymbol(source.getSymbol());
            if(cryptoCoinOptional.isPresent()) {
                CryptoCoin target = cryptoCoinOptional.get();
                if(!target.getUsd_price().equals(source.getBigDecimalPrice_usd())) {
                    target.setUsd_price(source.getBigDecimalPrice_usd());
                    userNotifyService.checkPrice(this.repository.save(target));
                }
            } else {
                this.repository.save(new CryptoCoin(source));
            }
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
