package com.idfinance.cryptocurrency.service.cryptoCoinService;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.service.CoinLoreService.CoinLoreRequestProperty;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import com.idfinance.cryptocurrency.storage.api.ICryptoCoinRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CryptoCoinService implements ICryptoCoinService {

    private final ICryptoCoinRepository repository;
    private final ICoinLoreResponseService coinLoreResponseService;
    private final CoinLoreRequestProperty coinLoreRequestProperty;

    public CryptoCoinService(ICryptoCoinRepository repository, ICoinLoreResponseService coinLoreResponseService, CoinLoreRequestProperty coinLoreRequestProperty) {
        this.repository = repository;
        this.coinLoreResponseService = coinLoreResponseService;
        this.coinLoreRequestProperty = coinLoreRequestProperty;
    }



    @Override
    public List<CoinView> getAll() {
        return this.repository.findAll().stream().map(e -> new CoinView(e.getName(), e.getSymbol(), e.getUsd_price())).collect(Collectors.toList());
    }

    @Override
    public CoinView getCryptoCoinPrice(String symbol) {
        return null;
    }

    public void loadCoinsData() {
        try {
            for (Coin e:coinLoreRequestProperty.getCoinList()) {
                CompletableFuture.supplyAsync(() ->
                                this.coinLoreResponseService.getResponse((coinLoreRequestProperty.getBaseUrl() + "/?id=" + e.getId())))
                        .whenCompleteAsync((result, exp) -> {
                                    try {
                                        System.out.println(result.take());
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                        );
            }
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadCoinsData();
    }



    @EventListener(ApplicationReadyEvent.class)
    public void loadCoinsDataAfterStartup() {
        loadCoinsData();
    }

}
