package com.decagon.decatrade.dto;

import com.decagon.decatrade.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class StockDto {
    private String symbol;
    private long quantity;
    private BigDecimal lastPrice;
    private BigDecimal currentValue;
    private BigDecimal amountPaid;
    private double percentageChange;

    public static StockDto fromStock(Stock stock) {
        return StockDto.builder()
            .symbol(stock.getSymbol())
            .quantity(stock.getQuantity())
            .build();
    }
}
