package com.decagon.decatrade.controller;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.dto.TransactionDto;
import com.decagon.decatrade.dto.TransactionRequest;
import com.decagon.decatrade.dto.TransactionResponse;
import com.decagon.decatrade.model.Transaction;
import com.decagon.decatrade.security.CurrentUser;
import com.decagon.decatrade.security.UserPrincipal;
import com.decagon.decatrade.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Query;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody TransactionRequest transactionRequest) throws IOException {
        Transaction transaction = transactionService.save(currentUser.getId(), transactionRequest);
        return new ResponseEntity<>(new TransactionResponse(transaction.getReference(), transaction.getAmount()), HttpStatus.ACCEPTED);
    }

    @GetMapping("confirm")
    public ResponseEntity<TransactionResponse> confirmTransaction(@CurrentUser UserPrincipal currentUser, @Query("reference") String reference) throws IOException {
        Transaction transaction = transactionService.confirmTransaction(currentUser.getId(), reference);
        return new ResponseEntity<>(new TransactionResponse(transaction.getReference(), transaction.getAmount()), HttpStatus.CREATED);
    }

    @DeleteMapping("{reference}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTransaction(@CurrentUser UserPrincipal currentUser, @PathVariable String reference) throws IOException {
        transactionService.cancelTransaction(currentUser.getId(), reference);
    }

    @GetMapping()
    public ResponseEntity<List<TransactionDto>> getTransactions(@CurrentUser UserPrincipal currentUser) {
        List<TransactionDto> transactions = transactionService.getUserTransactions(currentUser.getId())
            .stream().map(txn -> {
                return TransactionDto.builder().amount(txn.getAmount())
                    .quantity(txn.getQuantity())
                    .symbol(txn.getSymbol())
                    .reference(txn.getReference())
                    .transactionType(txn.getTransactionType())
                    .transactionStatus(txn.getTransactionStatus())
                    .transactionDate(txn.getCreatedAt()).build();
            }).collect(Collectors.toList());

        return ok(transactions);
    }


}
