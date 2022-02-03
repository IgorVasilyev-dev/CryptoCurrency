package com.idfinance.cryptocurrency.storage.api;

import com.idfinance.cryptocurrency.model.CryptoCoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICryptoCoinRepository extends JpaRepository<CryptoCoin, Long> {

}
