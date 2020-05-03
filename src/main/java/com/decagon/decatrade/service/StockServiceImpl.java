package com.decagon.decatrade.service;

import com.decagon.decatrade.model.Stock;
import com.decagon.decatrade.model.Transaction;
import com.decagon.decatrade.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;


    @Override
    public Stock buy(final Transaction transaction) throws IOException {
        Stock stock;

        Optional<Stock> optionalStock = stockRepository.findByUserIdAndSymbol(transaction.getUserId(), transaction.getSymbol());
        if (optionalStock.isPresent()) {
            //add to position
            stock = optionalStock.get();
            stock.setQuantity(stock.getQuantity() + transaction.getQuantity());
        } else {
            stock = new Stock();
            stock.setSymbol(transaction.getSymbol());
            stock.setUserId(transaction.getUserId());
            stock.setQuantity(transaction.getQuantity());
        }

        return stockRepository.save(stock);
    }

    @Override
    public Stock sell(final Transaction transaction) throws IOException {

        return null;
    }
}
