package com.awagcodes.weatherapp.Model.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);

    @Delete
    void delete(Weather weather);

    @Query("SELECT * FROM weather_table")
    LiveData<List<Weather>> getAllData();

}
