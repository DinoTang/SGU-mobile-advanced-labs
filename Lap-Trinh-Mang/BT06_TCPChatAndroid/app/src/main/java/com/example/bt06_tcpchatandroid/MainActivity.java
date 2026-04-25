package com.example.bt06_tcpchatandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;

import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    Button btnServer, btnClient, btnSend;
    EditText edtMessage;
    TextView tvChat;

    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);
        btnSend = findViewById(R.id.btnSend);
        edtMessage = findViewById(R.id.edtMessage);
        tvChat = findViewById(R.id.tvChat);

        btnServer.setOnClickListener(v -> startServer());
        btnClient.setOnClickListener(v -> startClient());
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(5000);

                runOnUiThread(() ->
                        tvChat.append("\nĐang chờ client..."));

                socket = serverSocket.accept();

                setupStreams();

                runOnUiThread(() ->
                        tvChat.append("\nClient đã kết nối"));

                listenMessage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startClient() {
        new Thread(() -> {
            try {
                socket = new Socket("10.0.2.2", 5000);

                setupStreams();

                runOnUiThread(() ->
                        tvChat.append("\nĐã kết nối server"));

                listenMessage();

            } catch (Exception e) {
                runOnUiThread(() ->
                        tvChat.append("\nLỗi kết nối"));
            }
        }).start();
    }

    private void setupStreams() throws Exception {
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }

    private void sendMessage() {
        String msg = edtMessage.getText().toString();

        if (writer != null) {
            writer.println(msg);
            tvChat.append("\nTôi: " + msg);
            edtMessage.setText("");
        }
    }

    private void listenMessage() {
        new Thread(() -> {
            try {
                String msg;

                while ((msg = reader.readLine()) != null) {
                    String finalMsg = msg;

                    runOnUiThread(() ->
                            tvChat.append("\nBạn: " + finalMsg));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}