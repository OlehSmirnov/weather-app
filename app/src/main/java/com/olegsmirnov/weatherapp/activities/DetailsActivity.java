package com.olegsmirnov.weatherapp.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    public static final String INTENT_KEY_CONTACT1 = "intent_key_contact1";
    public static final String INTENT_KEY_CONTACT2 = "intent_key_contact2";
    public static final String INTENT_KEY_CONTACT3 = "intent_key_contact3";
    public static final String INTENT_KEY_CONTACT4 = "intent_key_contact4";
    public static final String INTENT_KEY_CONTACT5 = "intent_key_contact5";
    public static final String INTENT_KEY_CONTACT6 = "intent_key_contact6";
    public static final String INTENT_KEY_CONTACT7 = "intent_key_contact7";
    public static final String INTENT_KEY_CONTACT8 = "intent_key_contact8";
    public static final String INTENT_KEY_CONTACT9 = "intent_key_contact9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getSupportActionBar().hide();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.back_button);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        if (getIntent() != null) {
            String temperature = getIntent().getStringExtra(INTENT_KEY_CONTACT1);
            String pressure = getIntent().getStringExtra(INTENT_KEY_CONTACT2);
            String seaPressure = getIntent().getStringExtra(INTENT_KEY_CONTACT3);
            String humidity = getIntent().getStringExtra(INTENT_KEY_CONTACT4);
            String skyStatus = getIntent().getStringExtra(INTENT_KEY_CONTACT5);
            String cloudiness = getIntent().getStringExtra(INTENT_KEY_CONTACT6);
            String windSpeed = getIntent().getStringExtra(INTENT_KEY_CONTACT7);
            String unitsName = getIntent().getStringExtra(INTENT_KEY_CONTACT8);
            String windDirection = getIntent().getStringExtra(INTENT_KEY_CONTACT9);
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetails);
            detailsFragment.updateContent(temperature, pressure, seaPressure, humidity, skyStatus, cloudiness, windSpeed, unitsName, windDirection);
        }
    }
}
