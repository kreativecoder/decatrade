package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.dto.TransactionRequest;
import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.exception.NotFoundException;
import com.decagon.decatrade.model.Transaction;
import com.decagon.decatrade.repository.TransactionRepository;
import com.decagon.decatrade.service.StockService;
import com.decagon.decatrade.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static com.decagon.decatrade.dto.TransactionStatus.PENDING;
import static com.decagon.decatrade.dto.TransactionStatus.SUCCESSFUL;
import static com.decagon.decatrade.dto.TransactionType.BUY;
import static com.decagon.decatrade.dto.TransactionType.SELL;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final IEXServiceImpl iexService;
    private final TransactionRepository transactionRepository;
    private final StockService stockService;

    @Override
    public QuoteResponse getStockQuote(final String symbol) throws IOException {
        return iexService.getStockQuote(symbol);
    }

    @Override
    public Optional<Transaction> findByReference(final String reference) {
        return transactionRepository.findByReference(reference);
    }

    @Override
    public Transaction save(Long userId, final TransactionRequest transactionRequest) throws IOException {
        if (findByReference(transactionRequest.getReference()).isPresent()) {
            throw new BadRequestException("Transaction with reference exists");
        }

        //if sell, check is user has enough to sell
        if (transactionRequest.getTransactionType().equals(SELL)) {
            stockService.validateCanSell(userId, transactionRequest.getSymbol().toUpperCase(), transactionRequest.getQuantity());
        }

        Transaction transaction = new Transaction();
        transaction.setReference(transactionRequest.getReference());
        transaction.setQuantity(transactionRequest.getQuantity());
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setSymbol(transactionRequest.getSymbol().toUpperCase());
        transaction.setAmount(getTotalAmount(transactionRequest.getQuantity(), transactionRequest.getSymbol()));
        transaction.setTransactionStatus(PENDING);
        transaction.setUserId(userId);

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction confirmTransaction(final Long userId, final String reference) throws IOException {
        Optional<Transaction> optionalTransaction = findByReference(reference);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setTransactionStatus(SUCCESSFUL);

            transaction = transactionRepository.save(transaction);
            if (transaction.getTransactionType().equals(BUY)) {
                stockService.buy(transaction);
            } else {
                stockService.sell(transaction);
            }

            return transaction;
        } else {
            throw new NotFoundException("Transaction not found.");
        }
    }

    private BigDecimal getTotalAmount(long quantity, String symbol) throws IOException {
        QuoteResponse quoteResponse = getStockQuote(symbol);
        double currentPrice = quoteResponse.getLatestPrice();

        BigDecimal total = BigDecimal.valueOf(currentPrice).multiply(BigDecimal.valueOf(quantity));

        return total;
    }
}
