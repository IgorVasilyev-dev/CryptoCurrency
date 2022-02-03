package com.idfinance.cryptocurrency.controller.rest;

import com.idfinance.cryptocurrency.dto.CreateSubscriptionRequest;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/notify")
public class UserNotifyRestServlet {

    private final ICoinLoreResponseService coinLoreResponseService;

    public UserNotifyRestServlet(ICoinLoreResponseService coinLoreResponseService) {
        this.coinLoreResponseService = coinLoreResponseService;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<?> userNotify(@Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok().body(coinLoreResponseService.getAllAvailableCoins());
    }

}
