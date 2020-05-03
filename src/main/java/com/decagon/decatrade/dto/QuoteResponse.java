package com.decagon.decatrade.dto;

import lombok.Data;

@Data
public class QuoteResponse {
    private String symbol;
    private String companyName;
    private double latestPrice;
}
