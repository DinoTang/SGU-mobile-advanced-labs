package com.example.bt03_clientserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText edtMessage;
    Button btnSend;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.edtMessage = findViewById(R.id.edtMessage);
        this.btnSend = findViewById(R.id.btnSend);
        this.tvStatus = findViewById(R.id.tvStatus);

        this.btnSend.setOnClickListener(v -> this.sendMessage());
    }

    private void sendMessage(){
        String message = this.edtMessage.getText().toString().trim();

        if(message.isEmpty()) {
            tvStatus.setText("Vui lòng nhập tin nhắn!");
            return;
        }

        new Thread(() -> {
            try {
                Socket socket = new Socket("10.0.2.2", 5000);

                PrintWriter writer =
                        new PrintWriter(socket.getOutputStream(), true);
                writer.println(message);

                runOnUiThread(() -> {
                    this.tvStatus.setText("Đã gửi: " + message);
                    this.edtMessage.setText("");
                });

                socket.close();

            } catch (Exception e){
                runOnUiThread(() ->
                        this.tvStatus.setText("Không kết nối được server"));
            }
        }).start();
    }
}