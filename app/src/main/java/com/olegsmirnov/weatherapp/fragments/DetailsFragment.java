package com.olegsmirnov.weatherapp.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olegsmirnov.weatherapp.R;

public class DetailsFragment extends Fragment {

    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvSeaPressure;
    private TextView tvHumidity;
    private TextView tvSkyStatus;
    private TextView tvCloudiness;
    private TextView tvWindSpeed;
    private TextView tvWindDirection;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTemperature = (TextView) view.findViewById(R.id.detailedTemperatureTV);
        tvPressure = (TextView) view.findViewById(R.id.detailedPressureTV);
        tvSeaPressure = (TextView) view.findViewById(R.id.detailedSeaLvlPressureTV);
        tvHumidity = (TextView) view.findViewById(R.id.detailedHumidityTV);
        tvSkyStatus = (TextView) view.findViewById(R.id.detailedSkyDescriptionTV);
        tvCloudiness = (TextView) view.findViewById(R.id.detailedCloudinessTV);
        tvWindSpeed = (TextView) view.findViewById(R.id.detailedWindSpeedTV);
        tvWindDirection = (TextView) view.findViewById(R.id.detailedWindDirectionTV);
    }

    public void updateContent(String temperature, String pressure, String seaPressure, String humidity, String skyStatus,
                              String cloudiness, String windSpeed, String windDirection) {
        tvTemperature.setText("Temperature: " + temperature + "°");
        tvPressure.setText("Pressure: " + pressure + "hpa");
        tvSeaPressure.setText("Sea level pressure: " + seaPressure + "hpa");
        tvHumidity.setText("Humidty: " + humidity + "%");
        tvSkyStatus.setText("Sky description: " + skyStatus);
        tvCloudiness.setText("Cloudiness: " + cloudiness + "%");
        tvWindSpeed.setText("Wind speed: " + windSpeed + "m/s");
        int iWindDirection = Integer.parseInt(windDirection);
        if (iWindDirection > 338 || iWindDirection <= 22) {
            windDirection = "North ↑";
        }
        else if (iWindDirection > 22 && iWindDirection <= 68) {
            windDirection = "North East ↗";
        }
        else if (iWindDirection > 68 && iWindDirection <= 112) {
            windDirection = "East →";
        }
        else if (iWindDirection > 112 && iWindDirection <= 158) {
            windDirection = "South East ↘";
        }
        else if (iWindDirection > 158 && iWindDirection <= 202) {
            windDirection = "South ↓";
        }
        else if (iWindDirection > 202 && iWindDirection <= 248) {
            windDirection = "South West ↙";
        }
        else if (iWindDirection > 248 && iWindDirection <= 292) {
            windDirection = "West ←";
        }
        else if (iWindDirection > 292 && iWindDirection <= 338) {
            windDirection = "NorthWest ↖";
        }
        tvWindDirection.setText("Wind direction: " + windDirection);
    }
}
