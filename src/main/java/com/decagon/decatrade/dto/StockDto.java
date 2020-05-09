package com.decagon.decatrade.dto;

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
}
