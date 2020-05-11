package com.decagon.decatrade.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class PortfolioSummary {
    List<TransactionDto> recentTransactions;
    List<StockDto> stocks;
}
