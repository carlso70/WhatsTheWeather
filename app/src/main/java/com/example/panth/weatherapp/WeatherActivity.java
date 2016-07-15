package com.example.panth.weatherapp;

import android.app.ProgressDialog;
import android.media.Image;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panth.weatherapp.data.Channel;
import com.example.panth.weatherapp.service.WeatherServiceCallback;
import com.example.panth.weatherapp.service.YahooWeatherService;

import org.w3c.dom.Text;

public class WeatherActivity extends ActionBarActivity implements WeatherServiceCallback {

    // UI elements
    private TextView temperatureTextView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private ImageView weatherIconImageView;

    // Service
    private YahooWeatherService service;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....please wait");
        dialog.show();

        service.refreshWeather("Chicago, IL");
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
    }

    @Override
    public void serviceFailure(Exception e) {
        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        dialog.hide();
    }
}
