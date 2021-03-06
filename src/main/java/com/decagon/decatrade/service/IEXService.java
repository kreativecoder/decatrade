package com.decagon.decatrade.service;

import com.decagon.decatrade.dto.QuoteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface IEXService {

    @GET("stock/{symbol}/quote")
    Call<QuoteResponse> getStockQuote(@Path("symbol") String symbol, @Query("token") String apiToken);

    @GET("ref-data/symbols")
    Call<List<QuoteResponse>> getAllSymbols(@Query("token") String apiToken);

    @GET("stock/market/batch?types=price")
    Call<Map<String, QuoteResponse>> getBatchPrice(@Query("symbols") String symbols, @Query("token") String apiToken);
}
