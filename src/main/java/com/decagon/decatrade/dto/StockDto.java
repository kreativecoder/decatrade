package com.decagon.decatrade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDto {
    private String symbol;
    private long quantity;
}
