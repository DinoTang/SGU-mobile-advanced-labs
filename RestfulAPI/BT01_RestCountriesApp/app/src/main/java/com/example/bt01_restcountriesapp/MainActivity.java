package com.example.bt01_restcountriesapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt01_restcountriesapp.adapter.CountryAdapter;
import com.example.bt01_restcountriesapp.api.ApiService;
import com.example.bt01_restcountriesapp.api.RetrofitClient;
import com.example.bt01_restcountriesapp.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CountryAdapter adapter;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CountryAdapter();
        recyclerView.setAdapter(adapter);

        // Retrofit
        apiService = RetrofitClient.getClient()
                .create(ApiService.class);

        loadCountries();
    }

    private void loadCountries() {

        apiService.getAllCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                Log.d("API_CODE", "code = " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    List<Country> list = response.body();

                    Log.d("API_SIZE", "size = " + list.size());

                    adapter.setData(list);
                } else {
                    Log.e("API", "Response fail or null body");
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("API_FAIL", t.getMessage(), t);
            }
        });
    }
}