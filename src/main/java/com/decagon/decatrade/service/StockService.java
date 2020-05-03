package com.decagon.decatrade.service;

import com.decagon.decatrade.model.Stock;
import com.decagon.decatrade.model.Transaction;

import java.io.IOException;

public interface StockService {
    Stock buy(Transaction transaction) throws IOException;
    Stock sell(Transaction transaction) throws IOException;
}
