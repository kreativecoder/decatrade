package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<QuoteResponse> checkUserName(@PathVariable String symbol) throws IOException {
        QuoteResponse quoteResponse = transactionService.getStockQuote(symbol);

        return ok(quoteResponse);
    }
}
