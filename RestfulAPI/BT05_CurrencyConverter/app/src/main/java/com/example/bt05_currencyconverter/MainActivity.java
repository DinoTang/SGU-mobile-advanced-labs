package com.example.bt05_currencyconverter;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtUsd, edtCurrency;
    Button btnConvert;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsd = findViewById(R.id.edtUsd);
        edtCurrency = findViewById(R.id.edtCurrency);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        btnConvert.setOnClickListener(v -> {

            String usdText = edtUsd.getText().toString().trim();
            String code = edtCurrency.getText().toString().trim().toUpperCase();

            if (usdText.isEmpty() || code.isEmpty()) {
                tvResult.setText("Nhập đầy đủ dữ liệu");
                return;
            }

            double usd = Double.parseDouble(usdText);

            apiService.getRates().enqueue(new Callback<CurrencyResponse>() {
                @Override
                public void onResponse(Call<CurrencyResponse> call,
                                       Response<CurrencyResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        Double rate = response.body().rates.get(code);

                        if (rate != null) {
                            double result = usd * rate;

                            tvResult.setText(
                                    usd + " USD = " + result + " " + code
                            );
                        } else {
                            tvResult.setText("Không tìm thấy mã tiền tệ");
                        }
                    }
                }

                @Override
                public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                    tvResult.setText("Lỗi mạng");
                }
            });
        });
    }
}