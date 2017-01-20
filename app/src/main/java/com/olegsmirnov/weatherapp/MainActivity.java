package com.olegsmirnov.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

    private String CITY_NAME = "London,GB";
    private ImageView loupe;
    private EditText inputField;
    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loupe = (ImageView) findViewById(R.id.loupeImg);
        inputField = (EditText) findViewById(R.id.editCityET);
        cityName = (TextView) findViewById(R.id.cityNameTV);
        cityName.setText(CITY_NAME);
        inputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (loupe.getVisibility() == View.VISIBLE) loupe.setVisibility(View.INVISIBLE);
                if (b) {
                    inputField.setText("");
                    loupe.setVisibility(View.INVISIBLE);
                }
                if (!b) {
                    CITY_NAME = String.valueOf(inputField.getText());
                    loupe.setVisibility(View.VISIBLE);
                    inputField.setText("Type your city");
                    new FetchWeatherData().execute();
                }
            }
        });
        new FetchWeatherData().execute();
    }

    class FetchWeatherData extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String weatherJsonObj;

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + CITY_NAME + "&units=metric&appid=1d0625288d8deb30b4d2ce607191b3bb");
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
                CITY_NAME = dataJsonObj.getJSONObject("city").getString("name")
                        + ", " + dataJsonObj.getJSONObject("city").getString("country");
                cityName.setText(CITY_NAME);
                JSONArray mainWeatherList = dataJsonObj.getJSONArray("list");
                List <WeatherItem> resultWeatherList = new ArrayList<>();
                for (int i = 0; i < mainWeatherList.length(); i++) {
                    JSONObject weatherListItem = mainWeatherList.getJSONObject(i);
                    String unformatedDate = weatherListItem.getString("dt_txt");
                    String date = unformatedDate.substring(5, unformatedDate.length() - 3);

                    JSONObject weatherListItemMain = weatherListItem.getJSONObject("main");
                    int temperature = weatherListItemMain.getInt("temp");
                    int pressure = weatherListItemMain.getInt("pressure");
                    String humidity = weatherListItemMain.getString("humidity");

                    JSONArray weatherListItemWeather = weatherListItem.getJSONArray("weather");
                    JSONObject weatherListItemWeatherObj = weatherListItemWeather.getJSONObject(0);
                    String iconIdentifier = weatherListItemWeatherObj.getString("icon");

                    JSONObject windInfoItem = weatherListItem.getJSONObject("wind");
                    int windSpeed = windInfoItem.getInt("speed");

                    resultWeatherList.add(new WeatherItem(date, iconIdentifier, temperature, pressure, humidity, windSpeed));
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