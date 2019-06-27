package com.awagcodes.weatherapp.View.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.awagcodes.weatherapp.Model.Room.Weather;
import com.awagcodes.weatherapp.R;

import java.util.List;

public class CityWeatherListAdapter extends RecyclerView.Adapter<CityWeatherListAdapter.CityWeatherViewHolder> {

    private DeleteCityWeatherInterface deleteCityWeatherInterface;
    private OnViewClicked onViewClicked;
    private List<Weather> weatherList;

    public CityWeatherListAdapter(List<Weather> weatherList, DeleteCityWeatherInterface deleteCityWeatherInterface, OnViewClicked onViewClicked) {
        this.weatherList = weatherList;
        this.deleteCityWeatherInterface = deleteCityWeatherInterface;
        this.onViewClicked = onViewClicked;
    }

    @NonNull
    @Override
    public CityWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_weather_item,parent,false);
        return new CityWeatherViewHolder(view,deleteCityWeatherInterface,onViewClicked);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CityWeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);

        holder.tv_city_value.setText(weather.getName().toUpperCase());
        holder.tv_temp_value.setText(String.format("%.2f",weather.getMaxTemp())+" °C");
        holder.tv_date_value.setText(weather.getLastUpdate());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public interface DeleteCityWeatherInterface{
        void onDeleteClicked(int position);
    }

    public interface OnViewClicked{
        void onViewClicked(int position);
    }

    public class CityWeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_city_value;
        private TextView tv_temp_value;
        private TextView tv_date_value;
        private Button btn_delete;
        private ConstraintLayout view_main;

        private DeleteCityWeatherInterface deleteCityWeatherInterface;
        private OnViewClicked onViewClicked;

        CityWeatherViewHolder(@NonNull View itemView, DeleteCityWeatherInterface deleteCityWeatherInterface,OnViewClicked onViewClicked) {
            super(itemView);

            tv_city_value = itemView.findViewById(R.id.tv_city_value);
            tv_temp_value = itemView.findViewById(R.id.tv_temp_value);
            tv_date_value = itemView.findViewById(R.id.tv_date_value);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            view_main = itemView.findViewById(R.id.view_main);

            this.deleteCityWeatherInterface = deleteCityWeatherInterface;
            this.onViewClicked = onViewClicked;

            btn_delete.setOnClickListener(this);
            view_main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_delete){
                deleteCityWeatherInterface.onDeleteClicked(getAdapterPosition());
            }else if(v.getId() == R.id.view_main){
                onViewClicked.onViewClicked(getAdapterPosition());
            }
        }
    }

}
