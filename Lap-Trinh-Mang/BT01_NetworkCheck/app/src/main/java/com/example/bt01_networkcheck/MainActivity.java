package com.example.bt01_networkcheck;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult;
    private Button btnCheck;

    private Button btnOpenGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        btnCheck = findViewById(R.id.btnCheck);
        btnOpenGoogle = findViewById(R.id.btnOpenGoogle);

        btnCheck.setOnClickListener(v -> {
            // Gọi hàm kiểm tra mạng
            checkInternetConnection();
        });

        btnOpenGoogle.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(intent);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkInternetConnection() {
        tvResult.setText("Đang kiểm tra...");
        // Mỗi lần kiểm tra mới thì tạm ẩn nút mở Google đi
        btnOpenGoogle.setVisibility(View.GONE);

        new Thread(() -> {
            boolean hasInternet = false;
            if (isNetworkAvailable()) {
                try {
                    // Sử dụng URL đề bài yêu cầu [cite: 6]
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    hasInternet = (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    hasInternet = false;
                }
            }

            boolean finalHasInternet = hasInternet;
            runOnUiThread(() -> {
                if (finalHasInternet) {
                    tvResult.setText("Kết nối Internet thành công!");
                    // 3. Hiện nút mở Google nếu thành công
                    btnOpenGoogle.setVisibility(View.VISIBLE);
                } else {
                    tvResult.setText("Không có kết nối Internet.");
                    btnOpenGoogle.setVisibility(View.GONE);
                }
            });
        }).start();
    }

}