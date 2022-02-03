package com.idfinance.cryptocurrency.service.cryptoCoinService;

import com.idfinance.cryptocurrency.dto.Coin;
import com.idfinance.cryptocurrency.dto.CoinView;
import com.idfinance.cryptocurrency.model.CryptoCoin;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import com.idfinance.cryptocurrency.storage.api.ICryptoCoinRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class CryptoCoinService implements ICryptoCoinService {

    private final ICryptoCoinRepository repository;
    private final ICoinLoreResponseService coinLoreResponseService;

    public CryptoCoinService(ICryptoCoinRepository repository, ICoinLoreResponseService coinLoreResponseService) {
        this.repository = repository;
        this.coinLoreResponseService = coinLoreResponseService;
    }



    @Override
    public List<CoinView> getAll() {
        return this.repository.findAll().stream().map(e -> new CoinView(e.getName(), e.getSymbol(), e.getUsd_price())).collect(Collectors.toList());
    }

    @Override
    public CoinView getCryptoCoinPrice(String symbol) {
        CryptoCoin cryptoCoin = this.repository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(format("Entity with symbol = %s Not Found", symbol)));
        return new CoinView(cryptoCoin.getName(), cryptoCoin.getSymbol(), cryptoCoin.getUsd_price());
    }

    private void loadCoinsData() {
        for (Coin e:coinLoreResponseService.getAllAvailableCoins()) {
            CompletableFuture.supplyAsync(() ->
                            this.coinLoreResponseService.getResponse(e.getId()))
                    .whenCompleteAsync((result, exp) -> {
                                if(exp == null) {
                                    try {
                                        CoinView coinView = result.take();
                                        Optional<CryptoCoin> cryptoCoinOptional = this.repository.findBySymbol(coinView.getSymbol());
                                        if(cryptoCoinOptional.isPresent()) {
                                            CryptoCoin cryptoCoin = cryptoCoinOptional.get();
                                            if(!cryptoCoin.getUsd_price().equals(coinView.getBigDecimalPrice_usd())) {
                                                cryptoCoin.setUsd_price(coinView.getBigDecimalPrice_usd());
                                                this.repository.save(cryptoCoin);
                                            }
                                        } else {
                                            this.repository.save(new CryptoCoin(coinView.getName(), coinView.getSymbol(), coinView.getBigDecimalPrice_usd()));
                                        }
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    System.out.println(exp.getMessage());
                                }
                            }
                    );
        }
        try {
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
