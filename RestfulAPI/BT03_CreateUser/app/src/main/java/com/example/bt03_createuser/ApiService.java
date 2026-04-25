package com.example.bt03_createuser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("users")
    Call<User> createUser(@Body User user);
}