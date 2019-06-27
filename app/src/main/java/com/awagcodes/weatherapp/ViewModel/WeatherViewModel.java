package com.awagcodes.weatherapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;
import com.awagcodes.weatherapp.WebApi.WeatherDataRepository;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private WeatherDataRepository weatherDataRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherDataRepository = new WeatherDataRepository(application);
    }

    public void insert(Weather weather){
        weatherDataRepository.insert(weather);
    }

    public void delete(Weather weather){
        weatherDataRepository.delete(weather);
    }

    public LiveData<List<Weather>> getAllWeatherData(){
        return weatherDataRepository.getAllWeatherData();
    }

    public LiveData<WeatherResponse> getCityWeather(String city,String api_key){
        return weatherDataRepository.getCityWeatherData(city,api_key);
    }

    public LiveData<String> getApiStatus(){
        return weatherDataRepository.apiResponseStatus();
    }

}
