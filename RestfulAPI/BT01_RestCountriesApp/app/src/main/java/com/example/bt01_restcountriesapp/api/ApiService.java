package com.example.bt01_restcountriesapp.api;


import com.example.bt01_restcountriesapp.model.Country;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {

    @GET("v3.1/all?fields=name,flags")
    Call<List<Country>> getAllCountries();
}