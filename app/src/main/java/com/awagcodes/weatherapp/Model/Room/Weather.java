package com.awagcodes.weatherapp.Model.Room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

// name of table weather table
@Entity(tableName = "weather_table")
public class Weather implements Parcelable {

    @PrimaryKey()
    @NotNull
    private String name;

    @ColumnInfo(name = "min_temp")
    private Double minTemp;

    @ColumnInfo(name = "max_temp")
    private Double maxTemp;

    @ColumnInfo(name = "last_update")
    private String lastUpdate;

    private Double humidity;

    @ColumnInfo(name = "wind_speed")
    private Double windSpeed;

    public Weather(@NotNull String name, Double minTemp, Double maxTemp, String lastUpdate, Double humidity, Double windSpeed) {

        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.lastUpdate = lastUpdate;
        this.humidity = humidity;
        this.windSpeed = windSpeed;

    }

    protected Weather(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            minTemp = null;
        } else {
            minTemp = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxTemp = null;
        } else {
            maxTemp = in.readDouble();
        }
        lastUpdate = in.readString();
        if (in.readByte() == 0) {
            humidity = null;
        } else {
            humidity = in.readDouble();
        }
        if (in.readByte() == 0) {
            windSpeed = null;
        } else {
            windSpeed = in.readDouble();
        }
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @NotNull
    public String getName() {
        return name;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (minTemp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minTemp);
        }
        if (maxTemp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(maxTemp);
        }
        dest.writeString(lastUpdate);
        if (humidity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(humidity);
        }
        if (windSpeed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(windSpeed);
        }
    }
}
