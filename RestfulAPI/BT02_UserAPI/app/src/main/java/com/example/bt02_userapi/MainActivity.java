package com.example.bt02_userapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<User> list = response.body();

                    StringBuilder sb = new StringBuilder();

                    for (User u : list) {
                        sb.append("ID: ").append(u.id).append("\n")
                                .append("Name: ").append(u.name).append("\n")
                                .append("Email: ").append(u.email).append("\n")
                                .append("Phone: ").append(u.phone).append("\n\n");
                    }

                    tvResult.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("API", t.getMessage(), t);
                tvResult.setText("Error: " + t.getMessage());
            }
        });
    }
}