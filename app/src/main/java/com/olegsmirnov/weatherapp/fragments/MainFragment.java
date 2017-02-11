package com.olegsmirnov.weatherapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.adapters.WeatherAdapter;
import com.olegsmirnov.weatherapp.entities.WeatherItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private String UNITS_NAME = "metric";
    private String CITY_NAME = "Cherkasy";

    private TextView tvCityName;
    private EditText etInputField;

    List<WeatherItem> weatherItems;

    private OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }
        else {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        }
        setHasOptionsMenu(true);
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            final View customView = (LinearLayout) getLayoutInflater(savedInstanceState)
                    .inflate(R.layout.dialog, null);
            settings.edit().putBoolean("my_first_time", false).apply();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Input your city here")
                    .setCancelable(false)
                    .setView(customView)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    EditText et = (EditText) customView.findViewById(R.id.et_dialog_city_name);
                                    CITY_NAME = et.getText().toString();
                                    new FetchWeatherData().execute();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        etInputField = (EditText) view.findViewById(R.id.editCityET);
        tvCityName = (TextView) view.findViewById(R.id.cityNameTV);
        tvCityName.setText(CITY_NAME);
        Button bSearch = (Button) view.findViewById(R.id.button_find_city);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CITY_NAME = String.valueOf(etInputField.getText());
                if (isNetworkAvailable()) {
                    new FetchWeatherData().execute();
                }
                else {
                    Toast.makeText(getContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
                }
                }

        });
        etInputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    CITY_NAME = String.valueOf(etInputField.getText());
                    new FetchWeatherData().execute();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isNetworkAvailable()) {
            switch (item.getItemId()) {
                case R.id.menu_celsius:
                    UNITS_NAME = "metric";
                    new FetchWeatherData().execute();
                    break;
                case R.id.menu_fahrenheit:
                    UNITS_NAME = "imperial";
                    new FetchWeatherData().execute();
                    break;
                case R.id.menu_kelvin:
                    UNITS_NAME = "default";
                    new FetchWeatherData().execute();
                    break;
                case R.id.menu_update:
                    new FetchWeatherData().execute();
                    break;
            }
        }
        else {
            Toast.makeText(getContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private class FetchWeatherData extends AsyncTask<Void, Void, String> {

        FetchWeatherData() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String weatherJsonObj;
            if (isNetworkAvailable()) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + CITY_NAME + "&units=" + UNITS_NAME + "&appid=1d0625288d8deb30b4d2ce607191b3bb");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");

                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    weatherJsonObj = buffer.toString();
                } catch (IOException e) {
                    return null;
                }
                return weatherJsonObj;
            }
            Toast.makeText(getContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            if (s != null) {
                try {
                    JSONObject dataJsonObj = new JSONObject(s);
                    CITY_NAME = dataJsonObj.getJSONObject("city").getString("name");
                    if (!CITY_NAME.toLowerCase().equals(etInputField.getText().toString().toLowerCase())
                            && !etInputField.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Incorrect city name, close result was shown", Toast.LENGTH_SHORT).show();
                    }
                    tvCityName.setText(CITY_NAME);
                    weatherItems = new ArrayList<>();
                    JSONArray mainWeatherList = dataJsonObj.getJSONArray("list");
                    for (int i = 0; i < mainWeatherList.length(); i++) {
                        JSONObject weatherListItem = mainWeatherList.getJSONObject(i);
                        String unformattedDate = weatherListItem.getString("dt_txt");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        Date myDate = formatter.parse(unformattedDate);
                        formatter.applyPattern("EEEE HH:mm");
                        String date = formatter.format(myDate);

                        int cloudiness = weatherListItem.getJSONObject("clouds").getInt("all");
                        JSONObject weatherListItemMain = weatherListItem.getJSONObject("main");
                        String temperature = String.valueOf(weatherListItemMain.getInt("temp"));
                        switch (UNITS_NAME) {
                            case "metric":
                                temperature += "°C";
                                break;
                            case "default":
                                temperature += "°K";
                                break;
                            case "imperial":
                                temperature += "°F";
                                break;
                        }
                        int pressure = weatherListItemMain.getInt("pressure");
                        int seaPressure = weatherListItemMain.getInt("sea_level");
                        int humidity = weatherListItemMain.getInt("humidity");

                        JSONArray weatherListItemWeather = weatherListItem.getJSONArray("weather");
                        JSONObject weatherListItemWeatherObj = weatherListItemWeather.getJSONObject(0);
                        String iconIdentifier = weatherListItemWeatherObj.getString("icon");
                        String skyStatus = weatherListItemWeatherObj.getString("description");

                        JSONObject windInfoItem = weatherListItem.getJSONObject("wind");
                        int windSpeed = windInfoItem.getInt("speed");
                        int windDirection = windInfoItem.getInt("deg");
                        WeatherItem item = new WeatherItem(date, iconIdentifier, temperature, String.valueOf(pressure), String.valueOf(seaPressure),
                                String.valueOf(humidity), String.valueOf(windSpeed), skyStatus, String.valueOf(cloudiness), UNITS_NAME, String.valueOf(windDirection));
                        weatherItems.add(item);
                    }
                    mRecyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    WeatherAdapter mAdapter = new WeatherAdapter(weatherItems);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (mListener != null) {
                                mListener.onFragmentInteraction(weatherItems.get(position).getTemperature(), weatherItems.get(position).getPressure(), weatherItems.get(position).getSeaPressure(),
                                        weatherItems.get(position).getHumidity(), weatherItems.get(position).getSkyStatus(), weatherItems.get(position).getCloudiness(),
                                        weatherItems.get(position).getWindSpeed(), UNITS_NAME, weatherItems.get(position).getWindDirection());
                            }
                        }
                    });

                } catch (JSONException | ParseException e) {
                    Toast.makeText(getContext(), "Incorrect city name", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getContext(), "Incorrect city name", Toast.LENGTH_SHORT).show();
            }
            etInputField.setText("");
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FetchWeatherData().execute();
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
                                   String cloudiness, String windSpeed, String unitsName, String windDirection);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
