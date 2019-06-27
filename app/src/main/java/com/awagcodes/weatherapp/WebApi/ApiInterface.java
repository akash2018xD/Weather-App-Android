package com.awagcodes.weatherapp.WebApi;

import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET(".")
    Call<WeatherResponse> getWeatherForCity(@Query("q") String city,@Query("appid") String appID);
}
