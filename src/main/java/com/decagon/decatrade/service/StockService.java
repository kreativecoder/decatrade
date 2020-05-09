package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.model.Stock;
import com.decagon.decatrade.model.Transaction;

import java.io.IOException;
import java.util.List;

public interface StockService {
    Stock buy(Transaction transaction) throws IOException;

    Stock sell(Transaction transaction) throws IOException;

    void validateCanSell(long userId, String symbol, long quantity);

    List<Stock> getAllStocks(long userId);

    List<QuoteResponse> getAllSymbols() throws IOException;
}
