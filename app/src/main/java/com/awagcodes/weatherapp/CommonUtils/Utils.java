package com.awagcodes.weatherapp.CommonUtils;

import android.annotation.SuppressLint;

import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    private static Double kelvinToCelsius(Double kelvin){
        return (kelvin - 273.15);
    }

    public static Weather weatherResponseToWeather(WeatherResponse response){
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEE, h:mm a");
        String strDate = sdf.format(c.getTime());

        return new Weather(response.getCity().getName().toUpperCase(),
                kelvinToCelsius(response.getList().get(0).getMain().getTempMin()),
                kelvinToCelsius(response.getList().get(0).getMain().getTempMax()),
                strDate,
                response.getList().get(0).getMain().getHumidity(),
                response.getList().get(0).getWind().getSpeed());
    }

}
