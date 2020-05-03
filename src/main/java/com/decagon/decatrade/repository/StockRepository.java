package com.decagon.decatrade.repository;

import com.decagon.decatrade.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByUserId(long userId);

    Optional<Stock> findByUserIdAndSymbol(long userId, String symbol);
}
