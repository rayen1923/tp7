package com.example.tp7;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("https://iset.alwaysdata.net/register.php")
    Call<ServerResponse> registerUser(@Body User user);

    @POST("https://iset.alwaysdata.net/login.php")
    Call<ServerResponse> getUser(@Body LoginRequest user);
}
