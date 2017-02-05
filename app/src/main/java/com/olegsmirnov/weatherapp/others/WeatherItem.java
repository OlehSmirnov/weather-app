package com.olegsmirnov.weatherapp.others;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherItem implements Parcelable {

    private String name;
    private String date;
    private String iconIdentifier;
    private String temperature;
    private String pressure;
    private String seaPressure;
    private String humidity;
    private String windSpeed;
    private String skyStatus;
    private String cloudiness;
    private String unitsName;
    private String windDirection;

    public WeatherItem(String date, String iconIdentifier, String temperature, String pressure, String seaPressure,
                       String humidity, String windSpeed, String skyStatus, String cloudiness, String unitsName, String windDirection) {
        this.date = date;
        this.iconIdentifier = iconIdentifier;
        this.temperature = temperature;
        this.pressure = pressure;
        this.seaPressure = seaPressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.skyStatus = skyStatus;
        this.cloudiness = cloudiness;
        this.unitsName = unitsName;
        this.windDirection = windDirection;
    }

    private WeatherItem(Parcel in) {
        date = in.readString();
        iconIdentifier = in.readString();
        temperature = in.readString();
        pressure = in.readString();
        seaPressure = in.readString();
        humidity = in.readString();
        windSpeed = in.readString();
        skyStatus = in.readString();
        cloudiness = in.readString();
        windDirection = in.readString();
    }

    public static final Parcelable.Creator<WeatherItem> CREATOR = new Parcelable.Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(iconIdentifier);
        parcel.writeString(temperature);
        parcel.writeString(pressure);
        parcel.writeString(seaPressure);
        parcel.writeString(humidity);
        parcel.writeString(skyStatus);
        parcel.writeString(cloudiness);
        parcel.writeString(windSpeed);
        parcel.writeString(windDirection);
    }

    public String getUnitsName() {
        return unitsName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIconIdentifier() { return iconIdentifier; }

    public String getSeaPressure() {
        return seaPressure;
    }

    public String getSkyStatus() {
        return skyStatus;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
}
