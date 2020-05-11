package com.decagon.decatrade.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class PortfolioSummary {
    private List<TransactionDto> recentTransactions;
    private List<StockDto> stocks;
    private BigDecimal portfolioValue;
}
