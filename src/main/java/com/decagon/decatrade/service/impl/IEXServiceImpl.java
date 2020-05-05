package com.decagon.decatrade.service.impl;

import com.decagon.decatrade.dto.QuoteResponse;
import com.decagon.decatrade.service.IEXService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

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
}
