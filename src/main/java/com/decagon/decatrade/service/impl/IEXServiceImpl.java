package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.service.IEXService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IEXServiceImpl {
    private String apiToken;

    private IEXService iexService;

    public IEXServiceImpl(@Value("${iex.base.url}") String apiBaseUrl, @Value("${iex.token}") String apiToken) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        iexService = retrofit.create(IEXService.class);
        this.apiToken = apiToken;
    }

    public QuoteResponse getStockQuote(String symbol) throws IOException {
        Response<QuoteResponse> response = iexService.getStockQuote(symbol, apiToken).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
        }

        return response.body();
    }

    public List<QuoteResponse> getAllSymbols() throws IOException {
        Response<List<QuoteResponse>> response = iexService.getAllSymbols(apiToken).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
        }

        return response.body();
    }

    public Map<String, Double> getBatchPrice(String symbols) throws IOException {
        HashMap<String, Double> prices = new HashMap<>();
        Response<Map<String, QuoteResponse>> response = iexService.getBatchPrice(symbols, apiToken).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
        }

        assert response.body() != null;
        response.body().forEach((s, q) -> prices.put(s, q.getPrice()));

        return prices;
    }
}
