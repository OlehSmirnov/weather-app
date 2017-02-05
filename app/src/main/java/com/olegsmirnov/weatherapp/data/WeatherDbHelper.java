package com.olegsmirnov.weatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.olegsmirnov.weatherapp.data.WeatherContract.WeatherEntry;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherCities.db";
    private static final int DATABASE_VERSION = 6;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ("
                + WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_DATE + " TEXT, "
                + WeatherEntry.COLUMN_ICON_IDENTIFIER + " TEXT, "
                + WeatherEntry.COLUMN_TEMPERATURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_PRESSURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_SEA_PRESSURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_HUMIDITY + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_WIND_SPEED + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_SKY_STATUS + " TEXT, "
                + WeatherEntry.COLUMN_CLOUDINESS + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_WIND_DIRECTION + " INTEGER DEFAULT 0); ";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME + ";";
        String CREATE_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ("
                + WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_DATE + " TEXT, "
                + WeatherEntry.COLUMN_ICON_IDENTIFIER + " TEXT, "
                + WeatherEntry.COLUMN_TEMPERATURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_PRESSURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_SEA_PRESSURE + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_HUMIDITY + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_WIND_SPEED + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_SKY_STATUS + " TEXT, "
                + WeatherEntry.COLUMN_CLOUDINESS + " INTEGER DEFAULT 0, "
                + WeatherEntry.COLUMN_WIND_DIRECTION + " INTEGER DEFAULT 0); ";
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }
}
