package com.example.bt05_networkspeedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    Button btnTest;
    TextView tvResult;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btnTest);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        btnTest.setOnClickListener(v -> startSpeedTest());
    }

    private void startSpeedTest() {

        progressBar.setVisibility(View.VISIBLE);
        tvResult.setText("Đang kiểm tra...");

        new Thread(() -> {
            try {

                long startTime = System.currentTimeMillis();

                URL url = new URL("https://jsonplaceholder.typicode.com/posts");
                URLConnection connection = url.openConnection();

                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                int totalBytes = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    totalBytes += bytesRead;
                }

                long endTime = System.currentTimeMillis();

                double seconds = (endTime - startTime) / 1000.0;

                double kb = totalBytes / 1024.0;
                double speed = kb / seconds;

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setText(String.format("Tốc độ: %.2f KB/s", speed));
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setText("Lỗi kiểm tra mạng");
                });
            }
        }).start();
    }
}