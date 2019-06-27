package com.awagcodes.weatherapp.View.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.awagcodes.weatherapp.CommonUtils.Utils;
import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;
import com.awagcodes.weatherapp.R;
import com.awagcodes.weatherapp.ViewModel.WeatherViewModel;

public class WeatherDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private WeatherViewModel weatherViewModel;

    private TextView tv_city_name;
    private TextView tv_temp_min_value;
    private TextView tv_temp_max_value;
    private TextView tv_last_updated_value;
    private TextView tv_humidity_value;
    private TextView tv_wind_speed_value;
    private ProgressBar progress_details;
    private ImageView img_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        Bundle data = getIntent().getExtras();
        assert data != null;
        Weather weather = data.getParcelable("weather");

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        init();
        assert weather != null;
        setValue(weather);
    }

    private void init() {
        tv_city_name = findViewById(R.id.tv_city_name);
        tv_temp_min_value = findViewById(R.id.tv_temp_min_value);
        tv_temp_max_value = findViewById(R.id.tv_temp_max_value);
        tv_last_updated_value = findViewById(R.id.tv_last_updated_value);
        tv_humidity_value = findViewById(R.id.tv_humidity_value);
        tv_wind_speed_value = findViewById(R.id.tv_wind_speed_value);
        img_refresh = findViewById(R.id.img_refresh);
        progress_details = findViewById(R.id.progress_details);
        img_refresh.setOnClickListener(this);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setValue(Weather weather) {
        tv_city_name.setText(weather.getName());
        tv_temp_min_value.setText(String.format("%.2f",weather.getMinTemp())+" °C");
        tv_temp_max_value.setText(String.format("%.2f",weather.getMaxTemp())+" °C");
        tv_last_updated_value.setText(weather.getLastUpdate());
        tv_humidity_value.setText(weather.getHumidity()+" %");
        tv_wind_speed_value.setText(weather.getWindSpeed()+" Km/Hr");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_refresh && isNetworkAvailable()){

            Toast.makeText(this,"Refreshing ...",Toast.LENGTH_SHORT).show();

            progress_details.setVisibility(View.VISIBLE);
            img_refresh.setEnabled(false);

            weatherViewModel.getCityWeather(tv_city_name.getText().toString().toLowerCase(),"e8ad5c4c18b06296d0d4fac6cf215614")
                    .observe(WeatherDetailActivity.this, new Observer<WeatherResponse>() {
                @Override
                public void onChanged(final WeatherResponse weatherResponse) {
                    weatherViewModel.getApiStatus().observe(WeatherDetailActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {

                            progress_details.setVisibility(View.GONE);
                            img_refresh.setEnabled(true);

                            if(s.equals("API_SUCCESS")){

                                weatherViewModel.insert(Utils.weatherResponseToWeather(weatherResponse));

                                setValue(Utils.weatherResponseToWeather(weatherResponse));

                                Toast.makeText(WeatherDetailActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(WeatherDetailActivity.this, "Could Not Update!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this,"Net Not Available!",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
