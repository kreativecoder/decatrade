package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.QuoteResponse;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class IEXServiceImpl {
    private static final String API_BASE_URL = "https://cloud.iexapis.com/stable/";
    private static final String API_TOKEN = "pk_d024f60c01db434ebcc6a3aa20baeaf3";
    private IEXService iexService;

    public IEXServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        iexService = retrofit.create(IEXService.class);
    }

    public QuoteResponse getStockQuote(String symbol) throws IOException {
        Response<QuoteResponse> response = iexService.getStockQuote(symbol, API_TOKEN).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
        }

        return response.body();
    }
}
