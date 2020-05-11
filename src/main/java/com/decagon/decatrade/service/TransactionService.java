package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.PortfolioSummary;
import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.dto.TransactionRequest;
import com.decagon.decatrade.model.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    QuoteResponse getStockQuote(String symbol) throws IOException;

    Optional<Transaction> findByReference(String reference);

    Transaction save(Long userId, TransactionRequest transactionRequest) throws IOException;

    Transaction confirmTransaction(Long userId, String reference) throws IOException;

    void cancelTransaction(Long userId, String reference) throws IOException;

    List<Transaction> getUserTransactions(long userId, Date dateFrom, Date dateTo);

    PortfolioSummary getPortfolioSummary(long userId);
}
