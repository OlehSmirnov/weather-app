package com.olegsmirnov.weatherapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.entities.WeatherItem;
import com.olegsmirnov.weatherapp.fragments.DetailsFragment;
import com.olegsmirnov.weatherapp.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private boolean isTablet;

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
    }

    @Override
    public void onFragmentInteraction(String temperature, String pressure, String seaPressure, String humidity, String skyStatus,
                                      String cloudiness, String windSpeed, String unitsName, String windDirection) {
        if (isTablet) {
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetails);
            detailsFragment.updateContent(temperature, pressure, seaPressure, humidity, skyStatus, cloudiness, windSpeed, unitsName, windDirection);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT1, temperature);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT2, pressure);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT3, seaPressure);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT4, humidity);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT5, skyStatus);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT6, cloudiness);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT7, windSpeed);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT8, unitsName);
            intent.putExtra(DetailsActivity.INTENT_KEY_CONTACT9, windDirection);
            intent.putExtra("item", new WeatherItem("", "", "", "", "", "", "", "", "", "", ""));
            startActivity(intent);
        }
    }
}
