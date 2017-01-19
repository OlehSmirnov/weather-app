package com.olegsmirnov.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String IMG_URL = "http://openweathermap.org/img/w/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FetchWeatherData task = new FetchWeatherData();
        task.execute();
    }

    class FetchWeatherData extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String weatherJsonObj;

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=Cherkassy,us&units=metric&appid=1d0625288d8deb30b4d2ce607191b3bb");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");

                }
                if (buffer.length() == 0) {
                    return null;
                }
                weatherJsonObj = buffer.toString();
            }
            catch (IOException e) {
                return null;
            }
            return weatherJsonObj;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject dataJsonObj = new JSONObject(s);
                System.out.println(dataJsonObj);
                String cityName = dataJsonObj.getJSONObject("city").getString("name");
                JSONArray mainWeatherList = dataJsonObj.getJSONArray("list");
                List <WeatherItem> resultWeatherList = new ArrayList<>();
                for (int i = 0; i < mainWeatherList.length(); i++) {
                    JSONObject weatherListItem = mainWeatherList.getJSONObject(i);
                    String date = weatherListItem.getString("dt_txt");

                    JSONObject weatherListItemMain = weatherListItem.getJSONObject("main");
                    String temperature = weatherListItemMain.getString("temp");
                    String pressure = weatherListItemMain.getString("pressure");
                    String humidity = weatherListItemMain.getString("humidity");

                    JSONArray weatherListItemWeather = weatherListItem.getJSONArray("weather");
                    JSONObject weatherListItemWeatherObj = weatherListItemWeather.getJSONObject(0);
                    String iconIdentifier = weatherListItemWeatherObj.getString("icon");

                    JSONObject windInfoItem = weatherListItem.getJSONObject("wind");
                    String windSpeed = windInfoItem.getString("speed");

                    resultWeatherList.add(new WeatherItem(date, cityName, iconIdentifier, temperature, pressure, humidity, windSpeed));
                }
                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                WeatherAdapter mAdapter = new WeatherAdapter(resultWeatherList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}