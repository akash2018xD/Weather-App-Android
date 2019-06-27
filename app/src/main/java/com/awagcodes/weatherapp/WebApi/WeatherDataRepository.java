package com.awagcodes.weatherapp.WebApi;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.Model.Room.WeatherDao;
import com.awagcodes.weatherapp.Model.Room.WeatherDatabase;
import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataRepository {

    private static final String TAG = "WeatherDataRepository";

    private WeatherDao weatherDao;
    private LiveData<List<Weather>> allWeatherData;
    private MutableLiveData<WeatherResponse> cityWeatherLiveData;
    private MutableLiveData<String> responseStatus;

    private ApiInterface apiInterface;

    public WeatherDataRepository(Application application){
        WeatherDatabase database = WeatherDatabase.getInstance(application);
        weatherDao = database.weatherDao();
        allWeatherData = weatherDao.getAllData();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public void insert(final Weather weather){
        new Thread(new Runnable() {
            @Override
            public void run() {
                weatherDao.insert(weather);
            }
        }).start();
    }

    public void delete(final Weather weather){
        new Thread(new Runnable() {
            @Override
            public void run() {
                weatherDao.delete(weather);
            }
        }).start();
    }

    public LiveData<List<Weather>> getAllWeatherData(){
        return allWeatherData;
    }

    public LiveData<WeatherResponse> getCityWeatherData(String city,String api_key){
        cityWeatherLiveData = new MutableLiveData<>();
        responseStatus = new MutableLiveData<>();

        apiInterface.getWeatherForCity(city,api_key).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> response) {
                if(response.code() == HttpURLConnection.HTTP_OK){

                    responseStatus.setValue("API_SUCCESS");
                    cityWeatherLiveData.setValue(response.body());

                }else {
                    responseStatus.setValue(response.message());
                    Log.d(TAG,"Error On Response");
                    cityWeatherLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                responseStatus.setValue(t.getMessage());
                Log.d(TAG,"Api Failed :"+t.getMessage());
                cityWeatherLiveData.setValue(null);
            }
        });
        return cityWeatherLiveData;
    }

    public LiveData<String> apiResponseStatus(){
        return responseStatus;
    }
}
