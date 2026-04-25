package com.example.bt05_currencyconverter;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("v6/latest/USD")
    Call<CurrencyResponse> getRates();
}