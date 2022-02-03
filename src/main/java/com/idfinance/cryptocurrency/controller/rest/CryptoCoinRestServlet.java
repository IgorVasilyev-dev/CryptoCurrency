package com.idfinance.cryptocurrency.controller.rest;

import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/crypto")
public class CryptoCoinRestServlet {

    private final ICryptoCoinService cryptoCoinService;
    private final ICoinLoreResponseService coinLoreResponseService;

    public CryptoCoinRestServlet(ICryptoCoinService cryptoCoinService, ICoinLoreResponseService coinLoreResponseService) {
        this.cryptoCoinService = cryptoCoinService;
        this.coinLoreResponseService = coinLoreResponseService;
    }

    @GetMapping(value = "/all", produces = {"application/json"} )
    protected ResponseEntity<?> getCoinsList() {
        return ResponseEntity.ok(coinLoreResponseService.getAllAvailableCoins().get(1).getId());
    }

    @GetMapping(value = "/token/{symbol}", produces = {"application/json"} )
    protected ResponseEntity<?> getPriceCoin(@PathVariable String symbol) {
        return ResponseEntity.ok(cryptoCoinService.getCryptoCoinPrice(symbol));
    }

}
