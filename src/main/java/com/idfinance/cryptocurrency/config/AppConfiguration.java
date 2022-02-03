package com.idfinance.cryptocurrency.config;

import com.idfinance.cryptocurrency.service.CoinLoreService.CoinLoreRequestProperty;
import com.idfinance.cryptocurrency.service.CoinLoreService.CoinLoreResponseService;
import com.idfinance.cryptocurrency.service.CoinLoreService.api.ICoinLoreResponseService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.CryptoCoinService;
import com.idfinance.cryptocurrency.service.cryptoCoinService.api.ICryptoCoinService;
import com.idfinance.cryptocurrency.storage.api.ICryptoCoinRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.idfinance.cryptocurrency.config")
public class AppConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ICryptoCoinService cryptoCoinService(ICryptoCoinRepository repository,
                                         ICoinLoreResponseService coinLoreResponseService,
                                         CoinLoreRequestProperty coinLoreRequestProperty) {
        return new CryptoCoinService(repository, coinLoreResponseService, coinLoreRequestProperty);
    }

    @Bean
    CoinLoreRequestProperty coinLoreRequestProperty() {
        return new CoinLoreRequestProperty();
    }

    @Bean
    ICoinLoreResponseService coinLoreResponseService(RestTemplate restTemplate, CoinLoreRequestProperty coinLoreRequestProperty) {
        return new CoinLoreResponseService(restTemplate, coinLoreRequestProperty);
    }

}
