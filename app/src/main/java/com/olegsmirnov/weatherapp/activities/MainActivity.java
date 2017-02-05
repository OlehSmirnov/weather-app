package com.olegsmirnov.weatherapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.data.WeatherContract;
import com.olegsmirnov.weatherapp.data.WeatherDbHelper;
import com.olegsmirnov.weatherapp.others.FetchWeatherData;
import com.olegsmirnov.weatherapp.others.WeatherAdapter;
import com.olegsmirnov.weatherapp.others.WeatherItem;
import com.olegsmirnov.weatherapp.fragments.DetailsFragment;
import com.olegsmirnov.weatherapp.fragments.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private boolean isTablet;
    EditText etCityName;
    WeatherDbHelper mDbHelper;
    List<WeatherItem> weatherItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mDbHelper = new WeatherDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM cities";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        if (icount <= 0 && !isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),
                    "No internet connection and database is empty", Toast.LENGTH_LONG).show();
        }
        else if (!isNetworkAvailable() && icount > 0) {
            SQLiteDatabase dbr = mDbHelper.getReadableDatabase();

            String[] projection = {"*"};

            Cursor cursor = dbr.query(
                    WeatherContract.WeatherEntry.TABLE_NAME, // таблица
                    projection,            // столбцы
                    null,                  // столбцы для условия WHERE
                    null,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                 // порядок сортировки

            try {

                int dateColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
                int pressureColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE);
                int seaPressureColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SEA_PRESSURE);
                int humidityColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY);
                int temperatureColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE);
                int skyStatusColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SKY_STATUS);
                int windDirectionColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION);
                int windSpeedColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED);
                int cloudinessColumnIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_CLOUDINESS);

                while (cursor.moveToNext()) {
                    String currentSeaPressure = cursor.getString(seaPressureColumnIndex);
                    String currentSkyStatus = cursor.getString(skyStatusColumnIndex);
                    String currentWindDirection = cursor.getString(windDirectionColumnIndex);
                    String currentCloudiness = cursor.getString(cloudinessColumnIndex);
                    String currentWindSpeed = cursor.getString(windSpeedColumnIndex);
                    String currentTemperature = cursor.getString(temperatureColumnIndex);
                    String currentDate = cursor.getString(dateColumnIndex);
                    String currentHumidity = cursor.getString(humidityColumnIndex);
                    String currentPressure = cursor.getString(pressureColumnIndex);
                    WeatherItem item = new WeatherItem(currentDate, null, currentTemperature, currentPressure, currentSeaPressure,
                            currentHumidity, currentWindSpeed, currentSkyStatus, currentCloudiness, currentWindDirection);
                    weatherItems.add(item);
                }
                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                WeatherAdapter mAdapter = new WeatherAdapter(weatherItems);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            } finally {
                cursor.close();
            }
        }
        else {
            final String PREFS_NAME = "MyPrefsFile";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            if (settings.getBoolean("my_first_time", true)) {
                settings.edit().putBoolean("my_first_time", false).apply();
                final View view = getLayoutInflater()
                        .inflate(R.layout.dialog_city, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Input your city")
                        .setView(view)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        etCityName = (EditText) view.findViewById(R.id.et_dialog_city_name);
                                        dialog.cancel();
                                    }
                                });
                builder.show();
                new FetchWeatherData(getApplicationContext(), view, etCityName.getText().toString());
            }
        }
    }

    @Override
    public void onFragmentInteraction(String temperature, String pressure, String seaPressure, String humidity, String skyStatus,
                                      String cloudiness, String windSpeed, String windDirection) {
        if (isTablet) {
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetails);
            detailsFragment.updateContent(temperature, pressure, seaPressure, humidity, skyStatus, cloudiness, windSpeed, windDirection);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT1, temperature);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT2, pressure);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT3, seaPressure);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT4, humidity);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT5, skyStatus);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT6, cloudiness);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT7, windSpeed);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT8, windDirection);
            intent.putExtra("item", new WeatherItem("", "", "", "", "", "", "", "", "", ""));
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
