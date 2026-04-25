package com.example.bt01_restcountriesapp.model;

import java.util.List;

public class Country {

    public Name name;
    public Flags flags;

    public static class Name {
        public String common;
    }

    public static class Flags {
        public String png;
    }
}