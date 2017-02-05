package com.olegsmirnov.weatherapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.data.WeatherContract.WeatherEntry;
import com.olegsmirnov.weatherapp.data.WeatherDbHelper;
import com.olegsmirnov.weatherapp.others.FetchWeatherData;
import com.olegsmirnov.weatherapp.others.WeatherAdapter;
import com.olegsmirnov.weatherapp.others.WeatherItem;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private WeatherDbHelper mDbHelper;

    private OnFragmentInteractionListener mListener;
    private String CITY_NAME = "Cherkasy";
    private String UNITS_NAME = "metric";
    private EditText etInputField;
    List<WeatherItem> weatherItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mDbHelper = new WeatherDbHelper(getContext());
        etInputField = (EditText) view.findViewById(R.id.editCityET);
        TextView tvCityName = (TextView) view.findViewById(R.id.cityNameTV);
        tvCityName.setText(CITY_NAME);
        etInputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etInputField.setText("");
                }
                else {
                    CITY_NAME = String.valueOf(etInputField.getText());
                    etInputField.setText(R.string.not_your_city);
                    new FetchWeatherData(getContext(), getView(), etInputField.getText().toString());
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isNetworkAvailable()) {
            SQLiteDatabase dbr = mDbHelper.getReadableDatabase();

            String[] projection = {"*"};

            Cursor cursor = dbr.query(
                    WeatherEntry.TABLE_NAME, // таблица
                    projection,            // столбцы
                    null,                  // столбцы для условия WHERE
                    null,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                 // порядок сортировки

            try {
                int dateColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_DATE);
                int iconColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_ICON_IDENTIFIER);
                int pressureColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
                int seaPressureColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_SEA_PRESSURE);
                int humidityColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
                int temperatureColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_TEMPERATURE);
                int skyStatusColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_SKY_STATUS);
                int windDirectionColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_WIND_DIRECTION);
                int windSpeedColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED);
                int cloudinessColumnIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_CLOUDINESS);

                while (cursor.moveToNext()) {
                    String currentIcon = cursor.getString(iconColumnIndex);
                    String currentSeaPressure = cursor.getString(seaPressureColumnIndex);
                    String currentSkyStatus = cursor.getString(skyStatusColumnIndex);
                    String currentWindDirection = cursor.getString(windDirectionColumnIndex);
                    String currentCloudiness = cursor.getString(cloudinessColumnIndex);
                    String currentWindSpeed = cursor.getString(windSpeedColumnIndex);
                    String currentTemperature = cursor.getString(temperatureColumnIndex);
                    String currentDate = cursor.getString(dateColumnIndex);
                    String currentHumidity = cursor.getString(humidityColumnIndex);
                    String currentPressure = cursor.getString(pressureColumnIndex);
                    WeatherItem item = new WeatherItem(currentDate, currentIcon, currentTemperature, currentPressure, currentSeaPressure,
                            currentHumidity, currentWindSpeed, currentSkyStatus, currentCloudiness, currentWindDirection);
                    weatherItems.add(item);
                }
                RecyclerView myRecyclerview = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
                myRecyclerview.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                WeatherAdapter mAdapter = new WeatherAdapter(weatherItems);
                myRecyclerview.setLayoutManager(mLayoutManager);
                myRecyclerview.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mListener != null) {
                            mListener.onFragmentInteraction(weatherItems.get(position).getTemperature(), weatherItems.get(position).getPressure(), weatherItems.get(position).getSeaPressure(),
                                    weatherItems.get(position).getHumidity(), weatherItems.get(position).getSkyStatus(), weatherItems.get(position).getCloudiness(),
                                    weatherItems.get(position).getWindSpeed(), weatherItems.get(position).getWindDirection());
                        }
                    }
                });
            } finally {
                cursor.close();
            }
        }
        else {
            new FetchWeatherData(getContext(), getView(), CITY_NAME).execute();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String temperature, String pressure, String seaPressure, String humidity, String skyStatus,
                                   String cloudiness, String windSpeed, String windDirection);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
