package com.idfinance.cryptocurrency.controller.rest;

import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/crypto")
public class CryptoCoinRestServlet {

    private final ICryptoCoinService cryptoCoinService;

    public CryptoCoinRestServlet(ICryptoCoinService cryptoCoinService) {
        this.cryptoCoinService = cryptoCoinService;
    }

    @GetMapping(value = "/all", produces = {"application/json"} )
    protected ResponseEntity<?> getCoinsList() {
        return ResponseEntity.ok(cryptoCoinService.getAll());
    }

    @GetMapping(value = "/price/{symbol}", produces = {"application/json"} )
    protected ResponseEntity<?> getPriceCoin(@PathVariable String symbol) {
        return ResponseEntity.ok(cryptoCoinService.getCryptoCoinPrice(symbol));
    }

    @PostMapping(value = "/notify", consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<?> userAddNotify(@Valid @RequestBody CreateSubscriptionRequest request) {
        cryptoCoinService.addSubscription(request);
        return ResponseEntity.ok().body("вы подписаны на " + request.getSymbol());
    }

    @DeleteMapping(value = "/notify", consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<?> userDeleteNotify(@Valid @RequestBody CreateSubscriptionRequest request) {
        cryptoCoinService.deleteSubscription(request);
        return ResponseEntity.ok().body("вы удалили подписку на " + request.getSymbol());
    }

}
