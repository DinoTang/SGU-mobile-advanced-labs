package com.example.bt07_pinglatencychecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText edtHost;
    Button btnPing;
    TextView tvResult;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtHost = findViewById(R.id.edtHost);
        btnPing = findViewById(R.id.btnPing);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        btnPing.setOnClickListener(v -> startPing());
    }

    private void startPing() {

        String host = edtHost.getText().toString().trim();

        if (host.isEmpty()) {
            tvResult.setText("Nhập host");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvResult.setText("Đang ping...");

        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();

                java.net.Socket socket = new java.net.Socket();
                socket.connect(
                        new java.net.InetSocketAddress(host, 80),
                        3000
                );

                long end = System.currentTimeMillis();
                socket.close();

                long delay = end - start;

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setText("Độ trễ: " + delay + " ms");
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvResult.setText("Không thể kết nối server");
                });
            }
        }).start();
    }
}