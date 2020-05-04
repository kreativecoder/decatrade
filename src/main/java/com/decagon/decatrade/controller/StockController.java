package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.StockDto;
import com.decagon.decatrade.security.CurrentUser;
import com.decagon.decatrade.security.UserPrincipal;
import com.decagon.decatrade.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/stocks")
@Slf4j
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDto>> getUserStocks(@CurrentUser UserPrincipal currentUser) throws IOException {
        List<StockDto> stocks = stockService.getAllStocks(currentUser.getId()).stream().map(
            stock -> new StockDto(stock.getSymbol(), stock.getQuantity())
        ).collect(Collectors.toList());

        return ok(stocks);
    }

}