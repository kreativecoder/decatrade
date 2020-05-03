package com.decagon.decatrade.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String symbol;
    private long quantity;
    private TransactionType transactionType;
    private String reference;
}
