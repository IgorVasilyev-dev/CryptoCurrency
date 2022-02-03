package com.idfinance.cryptocurrency.storage.api;

import com.idfinance.cryptocurrency.model.CryptoCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICryptoCoinRepository extends JpaRepository<CryptoCoin, Long> {

    Optional<CryptoCoin> findBySymbol(String symbol);

}
