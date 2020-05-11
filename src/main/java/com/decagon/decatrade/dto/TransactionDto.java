package com.decagon.decatrade.dto;

import com.decagon.decatrade.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class TransactionDto {
    private String symbol;
    private long quantity;
    private BigDecimal totalAmount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String reference;
    private Date transactionDate;

    public static TransactionDto fromTransaction(Transaction txn) {
        return TransactionDto.builder().totalAmount(txn.getAmount())
            .quantity(txn.getQuantity())
            .symbol(txn.getSymbol())
            .reference(txn.getReference())
            .transactionType(txn.getTransactionType())
            .transactionStatus(txn.getTransactionStatus())
            .transactionDate(txn.getCreatedAt()).build();
    }
}
