package com.decagon.decatrade.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class QuoteResponse {
    private String symbol;
    private String companyName;
    private double latestPrice;
    private String name;
    private double price;
}
