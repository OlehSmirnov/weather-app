package com.olegsmirnov.weatherapp.data;

import android.provider.BaseColumns;

public class WeatherContract {
    private WeatherContract() {}

    public static abstract class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "cities";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "city";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ICON_IDENTIFIER = "icon";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_SEA_PRESSURE = "sea_pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_SKY_STATUS = "sky_status";
        public static final String COLUMN_CLOUDINESS = "cloudiness";
        public static final String COLUMN_WIND_DIRECTION = "wind_direction";
    }
}
