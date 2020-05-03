package com.decagon.decatrade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String reference;
    private BigDecimal totalAmount;
}
