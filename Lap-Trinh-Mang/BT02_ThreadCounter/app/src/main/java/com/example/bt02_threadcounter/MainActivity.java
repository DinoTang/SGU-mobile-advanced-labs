package com.example.bt02_threadcounter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult;
    private Button btnStart, btnStop;
    private ProgressBar progressBar;
    private boolean isRunning = false;
    private Thread counterThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        this.tvResult = findViewById(R.id.tvResult);
        this.btnStart = findViewById(R.id.btnStart);
        this.btnStop = findViewById(R.id.btnStop);
        this.progressBar = findViewById(R.id.progressBar);

        this.btnStart.setOnClickListener(v -> this.startCounting());
        this.btnStop.setOnClickListener(v -> this.stopCounting());
    }

    private void startCounting(){
        if(this.isRunning) return;

        this.isRunning = true;

        progressBar.setProgress(0);
        tvResult.setText("Bắt đầu...");

        counterThread = new Thread(() -> {
            for (int i = 1 ; i <= 10; i++){
                if(!this.isRunning) return;

                int number = i;

                runOnUiThread(() -> {
                   this.tvResult.setText(String.valueOf(number));
                   this.progressBar.setProgress(number * 10);
                });

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            runOnUiThread(() -> this.tvResult.setText("Hoàn thành!"));
            this.isRunning = false;
        });

        this.counterThread.start();
    }

    private void stopCounting(){
        this.isRunning = false;
        this.tvResult.setText("Đã dừng!");
    }
}