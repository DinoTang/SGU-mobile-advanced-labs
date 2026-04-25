package com.example.bt03_createuser;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnCreate;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.btnCreate);
        tvResult = findViewById(R.id.tvResult);

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        btnCreate.setOnClickListener(v -> {

            User newUser = new User(
                    "Khánh Test",
                    "khanh@example.com",
                    "0123456789"
            );

            apiService.createUser(newUser).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        User result = response.body();

                        tvResult.setText(
                                        "Name: " + result.name + "\n" +
                                        "Email: " + result.email + "\n" +
                                        "Phone: " + result.phone
                        );
                    } else {
                        tvResult.setText("POST failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("POST_ERROR", t.getMessage(), t);
                    tvResult.setText("Error: " + t.getMessage());
                }
            });
        });
    }
}