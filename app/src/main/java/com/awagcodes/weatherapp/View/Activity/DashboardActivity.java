package com.awagcodes.weatherapp.View.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awagcodes.weatherapp.CommonUtils.Utils;
import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.Model.WebService.WeatherResponse;
import com.awagcodes.weatherapp.R;
import com.awagcodes.weatherapp.View.Adapter.CityWeatherListAdapter;
import com.awagcodes.weatherapp.ViewModel.WeatherViewModel;

import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, CityWeatherListAdapter.DeleteCityWeatherInterface, CityWeatherListAdapter.OnViewClicked {

    private static final String TAG = "DashboardActivity";
    private WeatherViewModel weatherViewModel;
    private EditText et_city_name;
    private RecyclerView recycler_view_saved;
    private List<Weather> weatherList;
    private ProgressBar progress_dashboard;
    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        init();
        setObserverRoom();
    }

    private void setObserverRoom() {

        weatherViewModel.getAllWeatherData().observe(this, new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weathers) {
                if (weathers.isEmpty()) {
                    Toast.makeText(DashboardActivity.this, "No City Saved!", Toast.LENGTH_SHORT).show();
                    recycler_view_saved.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, Integer.toString(weathers.size()));
                    Collections.reverse(weathers);
                    weatherList = weathers;
                    recycler_view_saved.setVisibility(View.VISIBLE);
                    setRecyclerView();
                }
            }
        });
    }

    private void setRecyclerView() {
        CityWeatherListAdapter cityWeatherListAdapter = new CityWeatherListAdapter(weatherList, DashboardActivity.this, DashboardActivity.this);
            recycler_view_saved.setLayoutManager(new LinearLayoutManager(this));
            recycler_view_saved.setAdapter(cityWeatherListAdapter);
            recycler_view_saved.setItemAnimator(new DefaultItemAnimator());
            recycler_view_saved.setNestedScrollingEnabled(true);
    }

    private void init() {
        et_city_name = findViewById(R.id.et_city_name);
        btn_search = findViewById(R.id.btn_search);
        progress_dashboard = findViewById(R.id.progress_dashboard);
        recycler_view_saved = findViewById(R.id.recycler_view_saved);
        btn_search.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search && et_city_name.getText().toString().length()>0 && isNetworkAvailable()) {
            String city = et_city_name.getText().toString();

            closeKeyboard(this);

            progress_dashboard.setVisibility(View.VISIBLE);
            btn_search.setText("Wait ...");
            btn_search.setEnabled(false);

            weatherViewModel.getCityWeather(city.trim().toLowerCase(), "e8ad5c4c18b06296d0d4fac6cf215614").observe(DashboardActivity.this, new Observer<WeatherResponse>() {
                @Override
                public void onChanged(final WeatherResponse weatherResponse) {


                    weatherViewModel.getApiStatus().observe(DashboardActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {

                            progress_dashboard.setVisibility(View.GONE);
                            btn_search.setText("Search");
                            btn_search.setEnabled(true);

                            if (s.equals("API_SUCCESS")) {
                                Toast.makeText(DashboardActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                                weatherViewModel.insert(Utils.weatherResponseToWeather(weatherResponse));
                                weatherViewModel.getAllWeatherData();
                            }
                            else {
                                Toast.makeText(DashboardActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }else {
            if(et_city_name.getText().toString().length()>0){
                Toast.makeText(this,"Net Not Available!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Enter A City Name!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    @Override
    public void onDeleteClicked(int position) {
        weatherViewModel.delete(weatherList.get(position));
    }

    @Override
    public void onViewClicked(int position) {
        Intent intent = new Intent(DashboardActivity.this, WeatherDetailActivity.class);
        intent.putExtra("weather",weatherList.get(position));
        startActivity(intent);
    }

    public static void closeKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if(view != null){
            InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
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
