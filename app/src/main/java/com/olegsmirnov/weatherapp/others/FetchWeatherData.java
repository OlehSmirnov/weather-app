package com.olegsmirnov.weatherapp.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.data.WeatherContract.WeatherEntry;
import com.olegsmirnov.weatherapp.data.WeatherDbHelper;
import com.olegsmirnov.weatherapp.fragments.MainFragment;

public class FetchWeatherData extends AsyncTask<Void, Void, String> {

    private Context context;
    private View view;
    private String CITY_NAME = "";
    private String UNITS_NAME = "metric";
    private List<WeatherItem> weatherItems = new ArrayList<>();
    private MainFragment.OnFragmentInteractionListener mListener;

    WeatherDbHelper mDbHelper;

    public FetchWeatherData(Context context, View view, String CITY_NAME) {
        this.context = context;
        this.view = view;
        this.CITY_NAME = CITY_NAME;
    }

    @Override
    protected String doInBackground(Void... params) {
        String weatherJsonObj;
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
        }
        catch (IOException e) {
            return null;
        }
        return weatherJsonObj;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            try {
                mDbHelper = new WeatherDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                JSONObject dataJsonObj = new JSONObject(s);
                JSONArray mainWeatherList = dataJsonObj.getJSONArray("list");
                for (int i = 0; i < mainWeatherList.length(); i++) {
                    JSONObject weatherListItem = mainWeatherList.getJSONObject(i);
                    String unformatedDate = weatherListItem.getString("dt_txt");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date myDate = formatter.parse(unformatedDate);
                    formatter.applyPattern("EEEE HH:mm");
                    String date = formatter.format(myDate);

                    int cloudiness = weatherListItem.getJSONObject("clouds").getInt("all");
                    JSONObject weatherListItemMain = weatherListItem.getJSONObject("main");
                    int temperature = weatherListItemMain.getInt("temp");
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
                    WeatherItem item = new WeatherItem(date, iconIdentifier, String.valueOf(temperature), String.valueOf(pressure), String.valueOf(seaPressure),
                            String.valueOf(humidity), String.valueOf(windSpeed), skyStatus, String.valueOf(cloudiness), String.valueOf(windDirection));
                    weatherItems.add(item);
                    ContentValues values = new ContentValues();
                    values.put(WeatherEntry.COLUMN_NAME, CITY_NAME);
                    values.put(WeatherEntry.COLUMN_DATE, date);
                    values.put(WeatherEntry.COLUMN_ICON_IDENTIFIER, iconIdentifier);
                    values.put(WeatherEntry.COLUMN_TEMPERATURE, String.valueOf(temperature));
                    values.put(WeatherEntry.COLUMN_PRESSURE, String.valueOf(pressure));
                    values.put(WeatherEntry.COLUMN_SEA_PRESSURE, String.valueOf(seaPressure));
                    values.put(WeatherEntry.COLUMN_HUMIDITY, String.valueOf(humidity));
                    values.put(WeatherEntry.COLUMN_WIND_SPEED, String.valueOf(windSpeed));
                    values.put(WeatherEntry.COLUMN_SKY_STATUS, skyStatus);
                    values.put(WeatherEntry.COLUMN_CLOUDINESS, String.valueOf(cloudiness));
                    values.put(WeatherEntry.COLUMN_WIND_DIRECTION, String.valueOf(windDirection));
                    db.update(WeatherEntry.TABLE_NAME, values, "city = ?", new String[]{"Cherkasy"});
                }

                RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                WeatherAdapter mAdapter = new WeatherAdapter(weatherItems);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
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

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
