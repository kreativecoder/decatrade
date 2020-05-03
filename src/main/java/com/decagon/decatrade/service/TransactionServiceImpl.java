package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.QuoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final IEXServiceImpl iexService;

    @Override
    public QuoteResponse getStockQuote(final String symbol) throws IOException {
        return iexService.getStockQuote(symbol);
    }
}
