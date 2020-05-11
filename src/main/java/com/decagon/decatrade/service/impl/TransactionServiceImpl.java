package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.dto.PortfolioSummary;
import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.dto.StockDto;
import com.decagon.decatrade.dto.TransactionDto;
import com.decagon.decatrade.dto.TransactionRequest;
import com.decagon.decatrade.exception.BadRequestException;
import com.decagon.decatrade.exception.NotFoundException;
import com.decagon.decatrade.model.Transaction;
import com.decagon.decatrade.model.User;
import com.decagon.decatrade.repository.TransactionRepository;
import com.decagon.decatrade.service.StockService;
import com.decagon.decatrade.service.TransactionService;
import com.decagon.decatrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.decagon.decatrade.dto.TransactionStatus.CANCELLED;
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
    private final UserService userService;

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
        double currentPrice = getStockQuote(transaction.getSymbol()).getLatestPrice();
        transaction.setAmount(getTotalAmount(transactionRequest.getQuantity(), currentPrice));
        transaction.setUnitPrice(currentPrice);
        transaction.setTransactionStatus(PENDING);
        transaction.setUserId(userId);

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction confirmTransaction(final Long userId, final String reference) throws IOException {
        Optional<Transaction> optionalTransaction = transactionRepository.findByReferenceAndUserId(reference, userId);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            //only confirm pending transaction
            if (transaction.getTransactionStatus().equals(PENDING)) {
                transaction.setTransactionStatus(SUCCESSFUL);

                transaction = transactionRepository.save(transaction);
                if (transaction.getTransactionType().equals(BUY)) {
                    stockService.buy(transaction);
                } else {
                    stockService.sell(transaction);
                }

                return transaction;
            } else {
                throw new BadRequestException("Transaction already completed.");
            }
        } else {
            throw new NotFoundException("Transaction not found.");
        }
    }

    @Override
    public void cancelTransaction(final Long userId, final String reference) throws IOException {
        Optional<Transaction> optionalTransaction = transactionRepository.findByReferenceAndUserId(reference, userId);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            //only cancel pending transaction
            if (transaction.getTransactionStatus().equals(PENDING)) {
                transaction.setTransactionStatus(CANCELLED);
                transactionRepository.save(transaction);
            } else {
                throw new BadRequestException("Transaction already completed.");
            }
        } else {
            throw new NotFoundException("Transaction not found.");
        }
    }

    @Override
    public List<Transaction> getUserTransactions(final long userId, Date dateFrom, Date dateTo) {
        //if dateFrom is not supplied, start from when user was created
        if (dateFrom == null) {
            Optional<User> optionalUser = userService.findById(userId);
            dateFrom = optionalUser.isPresent() ? optionalUser.get().getCreatedAt() : null;
        }

        //if dateTo is not supplied, use today
        if (dateTo == null) {
            dateTo = new Date();
        }

        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, dateFrom, dateTo);
    }

    @Override
    public PortfolioSummary getPortfolioSummary(final long userId) throws IOException {
        PortfolioSummary portfolioSummary = new PortfolioSummary();
        List<TransactionDto> recentTransactions = transactionRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(TransactionDto::fromTransaction).collect(Collectors.toList());

        List<StockDto> stocks = stockService.getAllStocks(userId).stream().map(StockDto::fromStock).collect(Collectors.toList());
        if (!stocks.isEmpty()) {
            List<String> symbols = stocks.stream().map(StockDto::getSymbol).collect(Collectors.toList());
            Map<String, Double> prices = iexService.getBatchPrice(String.join(",", symbols));
            BigDecimal value = BigDecimal.ZERO;

            for (StockDto stock : stocks) {
                value = value.add(BigDecimal.valueOf(prices.get(stock.getSymbol()))
                    .multiply(BigDecimal.valueOf(stock.getQuantity())));
            }
            portfolioSummary.setPortfolioValue(value);
        }

        portfolioSummary.setRecentTransactions(recentTransactions);
        return portfolioSummary;
    }

    @Override
    public List<StockDto> enrichStockDetails(final long userId, List<StockDto> stocks) throws IOException {
        if (!stocks.isEmpty()) {
            List<String> symbols = stocks.stream().map(StockDto::getSymbol).collect(Collectors.toList());
            Map<String, Double> prices = iexService.getBatchPrice(String.join(",", symbols));
            List<Map<String, BigDecimal>> amounts = transactionRepository.getAmountPaidByUser(userId);
            Map<String, BigDecimal> amountMap = new HashMap<>();
            for (Map amount : amounts) {
                amountMap.put((String) amount.get("symbol"), (BigDecimal) amount.get("amount"));
            }


            stocks.forEach(stock -> {
                BigDecimal price = BigDecimal.valueOf(prices.get(stock.getSymbol()));
                BigDecimal currentValue = price.multiply(BigDecimal.valueOf(stock.getQuantity()));
                BigDecimal amountPaid = amountMap.get(stock.getSymbol());

                double percentage = currentValue.subtract(amountPaid).divide(amountPaid, 4, RoundingMode.DOWN)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();

                stock.setLastPrice(price);
                stock.setCurrentValue(currentValue);
                stock.setAmountPaid(amountPaid);
                stock.setCurrentValue(currentValue);
                stock.setPercentageChange(percentage);
            });
        }

        return stocks;
    }

    private BigDecimal getTotalAmount(long quantity, double currentPrice) {

        //do gymnastics here to get total cost, add charges and all
        return BigDecimal.valueOf(currentPrice).multiply(BigDecimal.valueOf(quantity));
    }
}
