package com.olegsmirnov.weatherapp;

class WeatherItem {
    private String date;
    private String cityName;
    private String iconIdentifier;
    private String temperature;
    private String pressure;
    private String humidity;
    private String windSpeed;

    WeatherItem(String date, String cityName, String iconIdentifier, String temperature, String pressure, String humidity, String windSpeed) {
        this.date = date;
        this.cityName = cityName;
        this.iconIdentifier = iconIdentifier;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    String getDate() {
        return date;
    }

    String getCityName() {
        return cityName;
    }

    String getTemperature() {
        return temperature;
    }

    String getIconIdentifier() { return iconIdentifier; }

    String getPressure() {
        return pressure;
    }

    String getHumidity() {
        return humidity;
    }

    String getWindSpeed() {
        return windSpeed;
    }
}
