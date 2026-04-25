package com.example.bt04_weathermap;

import java.util.List;

public class WeatherResponse {

    public Main main;
    public List<Weather> weather;
    public String name;

    public static class Main {
        public float temp;
        public int humidity;
    }

    public static class Weather {
        public String description;
    }
}