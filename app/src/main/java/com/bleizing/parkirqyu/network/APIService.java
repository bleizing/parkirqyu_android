package com.bleizing.parkirqyu.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("user/login")
    Call<LoginResponse> processLogin(@Body LoginRequest loginRequest);
}
