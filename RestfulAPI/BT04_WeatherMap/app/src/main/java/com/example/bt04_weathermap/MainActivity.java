package com.example.bt04_weathermap;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtCity;
    Button btnSearch;
    TextView tvResult;

    String API_KEY = "c9bef73ee3b2e9b5132f689fe7c3da0b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCity = findViewById(R.id.edtCity);
        btnSearch = findViewById(R.id.btnSearch);
        tvResult = findViewById(R.id.tvResult);

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        btnSearch.setOnClickListener(v -> {

            String city = edtCity.getText().toString().trim();

            apiService.getWeather(city, API_KEY, "metric")
                    .enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                            if (response.isSuccessful() && response.body() != null) {

                                WeatherResponse data = response.body();

                                String result =
                                        "City: " + data.name + "\n" +
                                                "Temp: " + data.main.temp + "°C\n" +
                                                "Humidity: " + data.main.humidity + "%\n" +
                                                "Weather: " + data.weather.get(0).description;

                                tvResult.setText(result);

                            } else {
                                tvResult.setText("Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            Log.e("WEATHER_ERROR", t.getMessage(), t);
                            tvResult.setText("Failed: " + t.getMessage());
                        }
                    });
        });
    }
}