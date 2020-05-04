package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.exception.NotFoundException;
import com.decagon.decatrade.model.Stock;
import com.decagon.decatrade.model.Transaction;
import com.decagon.decatrade.repository.StockRepository;
import com.decagon.decatrade.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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
        Optional<Stock> optionalStock = stockRepository.findByUserIdAndSymbol(transaction.getUserId(), transaction.getSymbol());
        //sell position
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            stock.setQuantity(stock.getQuantity() - transaction.getQuantity());

            return stockRepository.save(stock);
        }

        //should not happen, as stock validation was done in the first request
        throw new IOException("Contact Admin.");
    }

    @Override
    public void validateCanSell(final long userId, final String symbol, long quantity) {
        Optional<Stock> optionalStock = stockRepository.findByUserIdAndSymbol(userId, symbol);
        //sell position
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            if (stock.getQuantity() < quantity) {
                throw new BadRequestException("User position is less than sell quantity.");
            }
        } else {
            throw new NotFoundException("User does not own stock.");
        }
    }

    @Override
    public List<Stock> getAllStocks(final long userId) {
        return stockRepository.findByUserId(userId);
    }
}
