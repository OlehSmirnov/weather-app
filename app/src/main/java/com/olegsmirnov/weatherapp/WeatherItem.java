package com.olegsmirnov.weatherapp;

class WeatherItem {
    private String date;
    private String iconIdentifier;
    private int temperature;
    private int pressure;
    private String humidity;
    private int windSpeed;

    WeatherItem(String date, String iconIdentifier, int temperature, int pressure, String humidity, int windSpeed) {
        this.date = date;
        this.iconIdentifier = iconIdentifier;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    String getDate() {
        return date;
    }

    int getTemperature() {
        return temperature;
    }

    String getIconIdentifier() { return iconIdentifier; }

    int getPressure() {
        return pressure;
    }

    String getHumidity() {
        return humidity;
    }

    int getWindSpeed() {
        return windSpeed;
    }
}
