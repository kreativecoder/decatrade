package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.QuoteResponse;

import java.io.IOException;

public interface TransactionService {
    QuoteResponse getStockQuote(String symbol) throws IOException;
}
