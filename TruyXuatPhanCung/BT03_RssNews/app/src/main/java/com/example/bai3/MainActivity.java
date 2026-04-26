package com.example.bai3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titles = new ArrayList<>();
    private ExecutorService executorService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        fetchRSS("https://vnexpress.net/rss/tin-moi-nhat.rss");
    }

    private void fetchRSS(String urlString) {
        executorService.execute(() -> {
            ArrayList<String> fetchedTitles = new ArrayList<>();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (InputStream inputStream = connection.getInputStream()) {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(inputStream, "UTF-8");

                        boolean insideItem = false;
                        String title = "";
                        int eventType = parser.getEventType();

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                if ("item".equalsIgnoreCase(parser.getName())) {
                                    insideItem = true;
                                } else if (insideItem && "title".equalsIgnoreCase(parser.getName())) {
                                    title = parser.nextText();
                                }
                            } else if (eventType == XmlPullParser.END_TAG
                                    && "item".equalsIgnoreCase(parser.getName())) {
                                fetchedTitles.add(title);
                                insideItem = false;
                            }

                            eventType = parser.next();
                        }
                    }
                }

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                titles.clear();
                titles.addAll(fetchedTitles);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}